package com.harshit.madad.authentication.util

sealed class AppScreen(val route: String) {
    object WelcomeScreen : AppScreen("welcome")
    object LoginScreen : AppScreen("login")
    object SignUpScreen : AppScreen("register")
    object HomeScreen : AppScreen("home")
}