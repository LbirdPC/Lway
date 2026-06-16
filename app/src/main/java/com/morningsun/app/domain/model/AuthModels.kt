package com.morningsun.app.domain.model

data class AuthUser(
    val uid: String,
    val email: String?
)

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class AuthUiState(
    val currentUser: AuthUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
