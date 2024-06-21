package com.harshit.madad.authentication.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.authentication.presentation.viewmodels.AuthViewModel
import com.harshit.madad.common.AppScreen
import com.harshit.madad.ui.theme.darkBlue
import com.harshit.madad.ui.theme.lightBlue

@Composable
fun WelcomeScreen(controller: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val backgroundImage: Painter = painterResource(id = R.drawable.welcome_logo)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = "Welcome background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.welcome_text),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.W600
            )
            Spacer(Modifier.height(18.dp))
            ElevatedButton(
                onClick = {
                    controller.navigate(AppScreen.RegisterScreen.LoginScreen.route)
                },
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 12.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.login_button_text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp),
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.register_helper_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                ClickableText(
                    text = buildAnnotatedString {
                        pushStringAnnotation(
                            tag = "REGISTER",
                            annotation = "register"
                        )
                        withStyle(
                            style = SpanStyle(
                                color = lightBlue,
                                fontWeight = FontWeight.W800
                            )
                        ) {
                            append(stringResource(id = R.string.register_button_text))
                        }
                        pop()
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    onClick = {
                        controller.navigate(AppScreen.RegisterScreen.SignUpScreen.route)
                    }
                )
            }
            Spacer(Modifier.height(40.dp))
        }
    }

}
