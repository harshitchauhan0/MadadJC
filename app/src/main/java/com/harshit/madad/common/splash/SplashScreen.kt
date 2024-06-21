package com.harshit.madad.common.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.common.AppScreen

@Composable
fun SplashScreen(controller: NavHostController, viewModel: SplashViewModel = hiltViewModel()) {
    val splashState by viewModel.splashState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.isLoggedIn()
    }

    LaunchedEffect(splashState) {
        when (splashState) {
            SplashState.Loading -> {

            }

            SplashState.NavigateToWelcome -> {
                controller.navigate(AppScreen.RegisterScreen.route) {
                    popUpTo(AppScreen.SplashScreen.route) { inclusive = true }
                }
            }

            SplashState.NavigateToHome -> {
                controller.navigate(AppScreen.MainScreen.HomeScreen.route) {
                    popUpTo(AppScreen.SplashScreen.route) { inclusive = true }
                }
            }

            SplashState.Error -> {

            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Splash Screen",
            modifier = Modifier.clip(CircleShape)
        )
    }
}
