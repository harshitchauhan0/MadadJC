package com.harshit.madad.authentication.util

import androidx.navigation.NavHostController

fun navigateToHome(controller: NavHostController) {
    controller.navigate(AppScreen.MainScreen.route) {
        popUpTo(AppScreen.RegisterScreen.WelcomeScreen.route) {
            inclusive = true
        }
    }
}