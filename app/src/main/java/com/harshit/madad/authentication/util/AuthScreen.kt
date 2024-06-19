package com.harshit.madad.authentication.util

sealed class AuthScreen(val route: String) {
    object WelcomeScreen : AuthScreen("welcome")
    object LoginScreen : AuthScreen("login")
    object SignUpScreen : AuthScreen("register")
}