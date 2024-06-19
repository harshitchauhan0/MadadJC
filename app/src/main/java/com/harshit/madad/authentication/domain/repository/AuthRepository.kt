package com.harshit.madad.authentication.domain.repository

interface AuthRepository {
    fun createUser(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailed: (Exception) -> Unit
    )

    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailed: (Exception) -> Unit
    )
}