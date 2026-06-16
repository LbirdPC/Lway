package com.morningsun.app.data.auth

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.morningsun.app.domain.model.AuthUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    private val auth: FirebaseAuth?
        get() = runCatching {
            val app = FirebaseApp.getInstance()
            if (FirebaseApp.getApps(app.applicationContext).isEmpty()) {
                null
            } else {
                FirebaseAuth.getInstance()
            }
        }.getOrNull()

    fun currentUser(): AuthUser? = auth?.currentUser?.let {
        AuthUser(uid = it.uid, email = it.email)
    }

    suspend fun signIn(email: String, password: String): Result<AuthUser> = runCatching {
        val firebaseAuth = requireAuth()
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        result.user?.let { AuthUser(uid = it.uid, email = it.email) }
            ?: error("No authenticated user returned.")
    }.recoverCatching { error ->
        throw IllegalStateException(error.toInlineMessage())
    }

    suspend fun register(email: String, password: String): Result<AuthUser> = runCatching {
        val firebaseAuth = requireAuth()
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let { AuthUser(uid = it.uid, email = it.email) }
            ?: error("No authenticated user returned.")
    }.recoverCatching { error ->
        throw IllegalStateException(error.toInlineMessage())
    }

    fun signOut() {
        auth?.signOut()
    }

    private fun requireAuth(): FirebaseAuth {
        return auth ?: error("Firebase is not configured. Add app/google-services.json and enable Firebase Auth.")
    }

    private fun Throwable.toInlineMessage(): String = when (this) {
        is FirebaseAuthInvalidCredentialsException -> "Email or password is incorrect."
        is FirebaseAuthInvalidUserException -> "No account found for this email."
        is FirebaseAuthUserCollisionException -> "This email is already registered."
        else -> message ?: "Network unavailable or Firebase is not configured."
    }
}
