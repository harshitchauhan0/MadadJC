package com.harshit.madad.authentication.presentation.components

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harshit.madad.R
import com.harshit.madad.authentication.presentation.viewmodels.AuthViewModel
import com.harshit.madad.ui.theme.PurpleGrey40
import com.harshit.madad.ui.theme.lightBlue
import com.harshit.madad.ui.theme.lightGreen

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigate: () -> Unit
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
        ForgetPassword { viewModel.showForgotPassword(true) }
        ErrorMessage(state.error)
        Spacer(modifier = Modifier.height(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 64.dp else 24.dp))
        SignInButton(viewModel, buttonEnabled, config)
    }

    AuthBottomEndImage()
    if (state.isLoading) {
        LoadingIndicator()
    } else if (state.isSignedIn) {
        onNavigate()
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
                        colors = ButtonDefaults.buttonColors(lightGreen)
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
        if (forgetState.isForgetPassword) {
            viewModel.showForgotPassword(false)
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
                tint = lightGreen
            )
        },
        placeholder = {
            Text(text = "Please Enter Your Email", color = PurpleGrey40)
        },
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = lightGreen
        )
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun ForgetPasswordHeading() {
    Text(
        text = "Forget Password?",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = lightGreen
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Enter your email address we'll send you \n a link to reset your password",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = lightGreen,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ForgetPassword(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Text(
            text = "Forget Password?",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W500,
            color = lightBlue,
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
            .fillMaxHeight(0.4f)
            .fillMaxWidth(0.7f)
    )
}

@Composable
fun WelcomeText() {
    Text(
        text = "Harshit Welcomes You",
        style = MaterialTheme.typography.headlineMedium,
        color = lightBlue,
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
                tint = lightBlue
            )
        },
        placeholder = {
            Text(text = "Please Enter Your Email", color = PurpleGrey40)
        },
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
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
                tint = lightBlue
            )
        },
        placeholder = {
            Text(text = "Enter Password", color = PurpleGrey40)
        },
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
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
            containerColor = lightBlue,
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottomcircle),
            contentDescription = "Auth Bottom End",
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
