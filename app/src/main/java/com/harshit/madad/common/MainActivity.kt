package com.harshit.madad.common

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.SEND_SMS
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
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
import com.harshit.madad.presentation.authentication.components.LoginScreen
import com.harshit.madad.presentation.authentication.components.SignUpScreen
import com.harshit.madad.presentation.authentication.components.WelcomeScreen
import com.harshit.madad.presentation.splash.SplashScreen
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.presentation.guardian.GuardianScreen
import com.harshit.madad.presentation.home.HomeScreen
import com.harshit.madad.presentation.message.MessageScreen
import com.harshit.madad.presentation.profile.ProfileScreen
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
                    navigation(route = AppScreen.RegisterScreen.route,
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
                        }) {
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
                    navigation(route = AppScreen.MainScreen.route,
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
                        }) {
                        composable(AppScreen.MainScreen.HomeScreen.route) {
                            HomeScreen(controller = navController,
                                onCallClick = ::callSuperGuardian,
                                onLogout = {
                                    navController.navigate(AppScreen.RegisterScreen.WelcomeScreen.route) {
                                        popUpTo(AppScreen.MainScreen.HomeScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                })
                        }
                        composable(AppScreen.MainScreen.MessageScreen.route) {
                            MessageScreen(controller = navController,
                                onHelpClick = { isCallingSelected, superGuardianNumber, guardianList, message ->
                                    messageGuardian(guardianList, message)
                                    if (isCallingSelected) {
                                        callSuperGuardian(superGuardianNumber)
                                    }
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
        if (!checkPermission(CALL_PHONE)) {
            requestCallPermission()
        }
    }

    private fun callSuperGuardian(number: String) {
        if (number.isBlank() or number.isEmpty()) {
            Toast.makeText(this, "Please select one super guardian", Toast.LENGTH_LONG).show()
            return
        }
        if (checkPermission(CALL_PHONE)) {
            makeCall(number)
        } else {
            requestCallPermission()
        }
    }

    private fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$number")
        }
        startActivity(intent)
        Log.v("TAGG", "callSuperGuardian: $number")
    }

    private fun requestCallPermission() {
        Toast.makeText(this, "Allow Call Permission", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    private fun messageGuardian(guardian: List<ContactItem>, message: String) {
        val res = StringBuilder()
        res.append(message)

        sendSms(guardian = guardian, message = res.toString())
    }

    private fun sendSms(guardian: List<ContactItem>, message: String) {
        if (checkPermission(SEND_SMS)) {
            if (guardian.isNotEmpty()) {
                try {
                    val smsManager = getSystemService(SmsManager::class.java)
                    guardian.forEach { contact ->
                        smsManager.sendTextMessage(contact.phoneNumber, null, message, null, null)
                    }
                    Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}