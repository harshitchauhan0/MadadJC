package com.harshit.madad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.harshit.madad.ui.theme.MadadTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.harshit.madad.authentication.presentation.components.LoginScreen
import com.harshit.madad.authentication.presentation.components.SignUpScreen
import com.harshit.madad.authentication.presentation.components.WelcomeScreen
import com.harshit.madad.authentication.util.AppScreen
import com.harshit.madad.authentication.util.navigateToHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MadadTheme {
                val controller = rememberNavController()
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    navController = controller,
                    startDestination = AppScreen.RegisterScreen.route,
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )
                    }
                ) {
                    navigation(
                        route = AppScreen.RegisterScreen.route,
                        startDestination = AppScreen.RegisterScreen.WelcomeScreen.route
                    ) {
                        composable(AppScreen.RegisterScreen.WelcomeScreen.route) {
                            WelcomeScreen(controller = controller)
                        }
                        composable(AppScreen.RegisterScreen.LoginScreen.route) {
                            LoginScreen(controller = controller)
                        }
                        composable(AppScreen.RegisterScreen.SignUpScreen.route) {
                            SignUpScreen(controller = controller)
                        }
                    }
                    navigation(
                        route = AppScreen.MainScreen.route,
                        startDestination = AppScreen.MainScreen.HomeScreen.route
                    ) {
                        composable(AppScreen.MainScreen.HomeScreen.route) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Hello"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}