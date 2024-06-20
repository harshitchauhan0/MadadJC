package com.harshit.madad.authentication.util

import androidx.navigation.NavHostController

fun navigateToHome(controller: NavHostController) {
    controller.navigate(AppScreen.HomeScreen.route) {
        popUpTo(AppScreen.WelcomeScreen.route) {
            inclusive = true
        }
    }
}