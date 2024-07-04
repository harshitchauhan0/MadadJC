package com.harshit.madad.presentation.authentication.components

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.presentation.authentication.viewmodel.AuthViewModel
import com.harshit.madad.common.navigateToHome
import com.harshit.madad.ui.theme.PurpleGrey40
import com.harshit.madad.ui.theme.blur
import com.harshit.madad.ui.theme.darkBlue
import com.harshit.madad.ui.theme.lightBlue
import com.harshit.madad.ui.theme.lightGrey
import com.harshit.madad.ui.theme.normalBlue

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    controller: NavHostController
) {
    val state by viewModel.signInState
    val config = LocalConfiguration.current
    val buttonEnabled = remember {
        derivedStateOf {
            viewModel.email.value.isNotBlank() && viewModel.passWord.value.isNotBlank()
        }
    }
    val forgetPasswordState by remember {
        derivedStateOf { viewModel.showForgotPasswordState.value }
    }
    BackHandler(enabled = forgetPasswordState) {
        viewModel.showForgotPassword(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blur)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AuthLogo()
        Spacer(modifier = Modifier.height(16.dp))
        WelcomeText()
        Spacer(modifier = Modifier.height(24.dp))
        EmailInput(viewModel, config)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordInput(viewModel, config)
        ForgetPassword(config = config, onClick = {
            viewModel.showForgotPassword(true)
        })
        ErrorMessage(state.error)
        Spacer(modifier = Modifier.height(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 64.dp else 24.dp))
        SignInButton(viewModel, buttonEnabled, config)
    }

    AuthBottomEndImage()

    if (state.isLoading) {
        LoadingIndicator()
    } else if (state.isSignedIn) {
        navigateToHome(controller)
    }
    AnimatedVisibility(
        visible = forgetPasswordState,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight }
        ) + fadeOut()
    ) {
        ForgetPasswordContent(viewModel, config)
    }
}

@Composable
fun ForgetPasswordContent(viewModel: AuthViewModel, config: Configuration) {
    val forgetState by remember {
        viewModel.forgetPasswordState
    }
    if (forgetState.isLoading) {
        LoadingIndicator()
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RemoveForgetPasswordBox(config) {
                viewModel.showForgotPassword(false)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 0.6f else 1.0f)
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ForgetPasswordHeading()
                    ForgetPasswordInput(viewModel, config)
                    ErrorMessage(error = forgetState.error)
                    Button(
                        onClick = viewModel::onForgetPassWord,
                        colors = ButtonDefaults.buttonColors(darkBlue)
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
        if (forgetState.isForgetPassword) {
            AlertDialogEmailCheck(
                message = "Please check your email you have received link to reset your password",
                onConfirm = { viewModel.showForgotPassword(false) }
            )
        }
    }
}

@Composable
fun AlertDialogEmailCheck(
    message: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xAA000000))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info Icon",
                tint = darkBlue,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = PurpleGrey40
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue)
            ) {
                Text(text = "OK")
            }
        }
    }
}


@Composable
fun RemoveForgetPasswordBox(config: Configuration, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 0.4f else 0.0f)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = onClick
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordInput(viewModel: AuthViewModel, config: Configuration) {
    OutlinedTextField(
        value = viewModel.forgetPasswordMail.value,
        onValueChange = viewModel::forgetPasswordMailChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email",
                tint = normalBlue
            )
        },
        placeholder = {
            Text(text = "Please Enter Your Email", color = PurpleGrey40)
        },
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = lightBlue
        ),
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun ForgetPasswordHeading() {
    Text(
        text = "Forget Password?",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = darkBlue
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Enter your email address we'll send you \n a link to reset your password",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = darkBlue,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ForgetPassword(onClick: () -> Unit, config: Configuration) {
    Box(
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = "Forget Password?",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W500,
            color = lightGrey,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(top = 8.dp)
        )
    }
}

@Composable
fun AuthLogo() {
    Image(
        painter = painterResource(id = R.drawable.auth_logo1),
        contentDescription = "Auth Logo",
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight(0.4f)
            .clip(RoundedCornerShape(20))
    )
}

@Composable
fun WelcomeText() {
    Text(
        text = "Harshit Welcomes You",
        style = MaterialTheme.typography.headlineMedium,
        color = Color.White,
        fontFamily = FontFamily.SansSerif
    )
}

@Composable
fun EmailInput(viewModel: AuthViewModel, config: Configuration) {
    OutlinedTextField(
        value = viewModel.email.value,
        onValueChange = viewModel::emailChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email",
                tint = normalBlue
            )
        },
        placeholder = {
            Text(text = "Please Enter Your Email", color = PurpleGrey40)
        },
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun PasswordInput(viewModel: AuthViewModel, config: Configuration) {
    OutlinedTextField(
        value = viewModel.passWord.value,
        onValueChange = viewModel::passwordChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "PassWord",
                tint = normalBlue
            )
        },
        placeholder = {
            Text(text = "Enter Password", color = PurpleGrey40)
        },
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun ErrorMessage(error: String?) {
    if (error?.isNotBlank() == true) {
        Text(
            text = error.toString(),
            color = Color.Red,
        )
    }
}

@Composable
fun SignInButton(viewModel: AuthViewModel, buttonEnabled: State<Boolean>, config: Configuration) {
    ElevatedButton(
        onClick = viewModel::onSignAccount,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White,
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 14.dp,
            pressedElevation = 8.dp
        ),
        enabled = buttonEnabled.value,
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        Text(text = "Let's get started")
    }
}

@Composable
fun AuthBottomEndImage() {
    val lightBlue = Color(0xFFA68CF8)
    val mediumBlue = Color(0xFFBBA7EC)
    val purple = Color(0xFFE2CCDC)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            // Draw background arcs
            drawArc(
                color = lightBlue,
                startAngle = 90f,
                sweepAngle = 360f,
                useCenter = true,
                topLeft = Offset(width * 0.4f, height * 0.8f),
                size = Size(width, height)
            )

            drawArc(
                color = mediumBlue,
                startAngle = 180f,
                sweepAngle = 100f,
                useCenter = true,
                topLeft = Offset(width * 0.2f, height * 0.8f),
                size = Size(width, height)
            )

            drawArc(
                color = purple,
                startAngle = 190f,
                sweepAngle = 100f,
                useCenter = true,
                topLeft = Offset(-width * 0.2f, height * 0.8f),
                size = Size(width, height)
            )
        }

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Star Icon",
            tint = Color.White,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background.copy(alpha = 0.2f)))
    Box(modifier = Modifier.fillMaxSize().clickable {  }, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = lightGrey)
    }
}
