package com.harshit.madad.authentication.util

sealed class AppScreen(val route: String) {
    object RegisterScreen: AppScreen("register_navigation_graph"){
        object WelcomeScreen : AppScreen("welcome")
        object LoginScreen : AppScreen("login")
        object SignUpScreen : AppScreen("register")
    }
    object MainScreen : AppScreen("home_navigation_graph"){
        object HomeScreen : AppScreen("home")
    }
}