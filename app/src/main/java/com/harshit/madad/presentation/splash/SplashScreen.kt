package com.harshit.madad.presentation.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.common.AppScreen

@Composable
fun SplashScreen(controller: NavHostController, viewModel: SplashViewModel = hiltViewModel()) {
    val splashState by viewModel.splashState.collectAsState()

    var isOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isOpen = true
        viewModel.isLoggedIn()
    }

    LaunchedEffect(splashState) {
        when (splashState) {
            SplashState.Loading -> {}
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
            SplashState.Error -> {}
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val size by infiniteTransition.animateFloat(
        initialValue = 100.0F,
        targetValue = 160.0F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600),
            repeatMode = RepeatMode.Reverse
        ), label = "Size"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Splash Screen",
            modifier = Modifier
                .clip(CircleShape)
                .size(size.dp),
        )
    }
}
