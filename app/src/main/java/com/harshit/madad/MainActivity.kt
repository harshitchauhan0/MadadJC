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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.harshit.madad.home.data.remote.dto.Guardian
import com.harshit.madad.home.presentation.components.GuardianScreen
import com.harshit.madad.home.presentation.components.HomeScreen
import com.harshit.madad.home.presentation.components.MessageScreen
import com.harshit.madad.home.presentation.components.ProfileScreen
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
                                onCallClick = ::callSuperGuardian
                            )
                        }
                        composable(AppScreen.MainScreen.MessageScreen.route) {
                            MessageScreen(controller = navController,onHelpClick = { superGuardianNumber, guardianList,message ->
                                callSuperGuardian(superGuardianNumber)
                                messageGuardian(guardianList,message)
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
//            val phoneIntent = Intent(Intent.ACTION_CALL).apply {
//                data = Uri.parse("tel: $number")
//            }
//            startActivity(phoneIntent)
        }
        Log.v("TAGGY",number)
    }
    private fun messageGuardian(guardian: List<Guardian>,message: String){
        Log.v("TAGGY",guardian.toString())
        Log.v("TAGGY",message)
    }

    private fun checkCallPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }
}