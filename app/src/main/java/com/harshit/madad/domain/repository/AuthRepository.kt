package com.harshit.madad.domain.repository

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

    fun forgetPassword(
        email: String,
        onSignInSuccess: () -> Unit,
        onSignInFailed: (Exception) -> Unit
    )

    fun checkUserExist(): Boolean
}