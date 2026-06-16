package com.morningsun.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morningsun.app.data.auth.AuthRepository
import com.morningsun.app.domain.model.AuthMode
import com.morningsun.app.domain.model.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(currentUser = authRepository.currentUser()))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun submit(mode: AuthMode, email: String, password: String) {
        val normalizedEmail = email.trim()
        val validationError = validate(normalizedEmail, password)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = when (mode) {
                AuthMode.LOGIN -> authRepository.signIn(normalizedEmail, password)
                AuthMode.REGISTER -> authRepository.register(normalizedEmail, password)
            }
            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(currentUser = user, isLoading = false, errorMessage = null)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Authentication failed.")
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun validate(email: String, password: String): String? = when {
        email.isBlank() || password.isBlank() -> "Email and password are required."
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email address."
        password.length < 6 -> "Password must be at least 6 characters."
        else -> null
    }
}
