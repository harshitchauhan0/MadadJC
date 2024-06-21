package com.harshit.madad.home.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.harshit.madad.common.AppScreen

@Immutable
data class FeatureItem(
    val backGroundColor: Color = Color.Transparent,
    val text: String = "",
    val imageVector: ImageVector? = null,
    val contentDescription: String? = null,
    val iconTint: Color = Color.Transparent,
    val route: String? = null
)

val featureList = listOf(
    FeatureItem(
        text = "Emergency Call",
        imageVector = Icons.Default.Phone,
        iconTint = Color(0xFFD78098),
        contentDescription = "Emergency Call",
        backGroundColor = Color(0xFFF1DFE4),
        route = null
    ), FeatureItem(
        text = "Send Message with Location",
        imageVector = Icons.Default.MailOutline,
        iconTint = Color(0xFFEF8D6E),
        contentDescription = "Emergency Message",
        backGroundColor = Color(0xFFFFECE5),
        route = AppScreen.MainScreen.MessageScreen.route
    ),

    FeatureItem(
        text = "Set Guardian",
        imageVector = Icons.Default.Favorite,
        iconTint = Color(0xFFC465C6),
        contentDescription = "Set Guardian",
        backGroundColor = Color(0xFFF6E7F7),
        route = AppScreen.MainScreen.GuardianScreen.route
    ),

    FeatureItem(
        text = "Set Up Profile",
        imageVector = Icons.Default.Person,
        iconTint = Color(0xFF5A8FAF),
        contentDescription = "Set Up Profile",
        backGroundColor = Color(0xFFD5E8ED),
        route = AppScreen.MainScreen.ProfileScreen.route
    )
)