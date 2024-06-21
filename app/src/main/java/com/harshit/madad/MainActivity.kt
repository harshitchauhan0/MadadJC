package com.harshit.madad

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.harshit.madad.authentication.presentation.components.LoadingIndicator
import com.harshit.madad.authentication.presentation.components.LoginScreen
import com.harshit.madad.authentication.presentation.components.SignUpScreen
import com.harshit.madad.authentication.presentation.components.WelcomeScreen
import com.harshit.madad.common.AppScreen
import com.harshit.madad.common.splash.SplashScreen
import com.harshit.madad.home.presentation.components.HomeScreen
import com.harshit.madad.home.presentation.viewmodels.HomeViewModel
import com.harshit.madad.ui.theme.MadadTheme
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
                    startDestination = AppScreen.SplashScreen.route
                ) {
                    composable(AppScreen.SplashScreen.route) {
                        SplashScreen(controller = controller)
                    }
                    navigation(route = AppScreen.RegisterScreen.route,
                        startDestination = AppScreen.RegisterScreen.WelcomeScreen.route,
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
                        }) {

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
                        composable(AppScreen.MainScreen.HomeScreen.route, enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        }, exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        }, popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        }, popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            )
                        }) {
                            HomeScreen(controller, onCallClick = { number ->
                                callSuperGuardian(number)
                            })
                        }
                        composable(AppScreen.MainScreen.MessageScreen.route) {

                        }
                        composable(AppScreen.MainScreen.GuardianScreen.route) {

                        }
                        composable(AppScreen.MainScreen.ProfileScreen.route) {

                        }
                    }
                }
            }
        }
    }

    private fun callSuperGuardian(number: String) {
        if (checkCallPermission()) {
            val phoneIntent = Intent(Intent.ACTION_CALL)
            phoneIntent.data = Uri.parse("tel: $number")
            startActivity(phoneIntent)
        }
        else{

        }
    }

    private fun checkCallPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }
}