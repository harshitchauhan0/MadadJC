package com.harshit.madad

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.madad.authentication.presentation.components.LoginScreen
import com.harshit.madad.authentication.presentation.components.SignUpScreen
import com.harshit.madad.authentication.presentation.components.WelcomeScreen
import com.harshit.madad.common.AppScreen
import com.harshit.madad.common.splash.SplashScreen
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.presentation.components.GuardianScreen
import com.harshit.madad.home.presentation.components.HomeScreen
import com.harshit.madad.home.presentation.components.MessageScreen
import com.harshit.madad.home.presentation.components.ProfileScreen
import com.harshit.madad.ui.theme.MadadTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MadadTheme {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    navController = navController,
                    startDestination = AppScreen.SplashScreen.route
                ) {
                    composable(AppScreen.SplashScreen.route) {
                        SplashScreen(controller = navController)
                    }
                    navigation(
                        route = AppScreen.RegisterScreen.route,
                        startDestination = AppScreen.RegisterScreen.WelcomeScreen.route,
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        }
                    ) {
                        composable(AppScreen.RegisterScreen.WelcomeScreen.route) {
                            WelcomeScreen(controller = navController)
                        }
                        composable(AppScreen.RegisterScreen.LoginScreen.route) {
                            LoginScreen(controller = navController)
                        }
                        composable(AppScreen.RegisterScreen.SignUpScreen.route) {
                            SignUpScreen(controller = navController)
                        }
                    }
                    navigation(
                        route = AppScreen.MainScreen.route,
                        startDestination = AppScreen.MainScreen.HomeScreen.route,
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
                        composable(AppScreen.MainScreen.HomeScreen.route) {
                            HomeScreen(
                                controller = navController,
                                onCallClick = ::callSuperGuardian,
                                onLogout = {
                                    navController.navigate(AppScreen.RegisterScreen.WelcomeScreen.route) {
                                        popUpTo(AppScreen.MainScreen.HomeScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        composable(AppScreen.MainScreen.MessageScreen.route) {
                            MessageScreen(
                                controller = navController,
                                onHelpClick = { superGuardianNumber, guardianList, message ->
                                    callSuperGuardian(superGuardianNumber)
                                    messageGuardian(guardianList, message)
                                })
                        }
                        composable(AppScreen.MainScreen.GuardianScreen.route) {
                            GuardianScreen(controller = navController)
                        }
                        composable(AppScreen.MainScreen.ProfileScreen.route) {
                            ProfileScreen(controller = navController)
                        }
                    }
                }
            }
        }
    }

    private fun callSuperGuardian(number: String) {
        if (checkCallPermission()) {

        }
    }

    private fun messageGuardian(guardian: List<ContactItem>, message: String) {

    }

    private fun checkCallPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }
}