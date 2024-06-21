package com.harshit.madad.common

sealed class AppScreen(val route: String) {
    object RegisterScreen: AppScreen("register_navigation_graph"){
        object WelcomeScreen : AppScreen("welcome")
        object LoginScreen : AppScreen("login")
        object SignUpScreen : AppScreen("register")
    }
    object MainScreen : AppScreen("home_navigation_graph"){
        object HomeScreen : AppScreen("home")
        object MessageScreen : AppScreen("message")
        object ProfileScreen : AppScreen("profile")
        object GuardianScreen : AppScreen("guardian")
    }
    object SplashScreen: AppScreen("splash")
}