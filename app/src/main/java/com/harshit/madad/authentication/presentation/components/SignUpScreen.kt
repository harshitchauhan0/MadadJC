package com.harshit.madad.authentication.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.harshit.madad.R
import com.harshit.madad.authentication.presentation.viewmodels.AuthViewModel
import com.harshit.madad.authentication.presentation.viewmodels.CreateAccountState
import com.harshit.madad.authentication.util.AppScreen
import com.harshit.madad.ui.theme.Pink80
import com.harshit.madad.ui.theme.darkBlue
import com.harshit.madad.ui.theme.lightGreen
import com.harshit.madad.ui.theme.lightOrange

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    controller: NavController,
) {
    val config = LocalConfiguration.current
    val state by viewModel.createState
    val buttonEnabled = remember {
        derivedStateOf {
            viewModel.email.value.isNotBlank() && viewModel.passWord.value.isNotBlank()
        }
    }
    if (state.isLoading) {
        LoadingIndicator()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBlue)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        LoginLogo()
        Spacer(modifier = Modifier.height(24.dp))
        EmailInput(viewModel, config)
        Spacer(modifier = Modifier.height(16.dp))

        PasswordInput(viewModel, config)
        ErrorLoginMessage(state = state)
        Spacer(modifier = Modifier.height(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 64.dp else 24.dp))
        LoginButton(viewModel, buttonEnabled, config)
    }

    AuthBackgroundImage()

     if (state.isAccountCreated) {
        controller.popBackStack()
        controller.navigate(AppScreen.RegisterScreen.LoginScreen.route)
    }
}

@Composable
fun LoginLogo() {
    Image(
        painter = painterResource(id = R.drawable.auth_logo),
        contentDescription = "Auth Logo",
        modifier = Modifier
            .fillMaxHeight(0.4f)
            .fillMaxWidth(0.7f).clip(RoundedCornerShape(20.dp))
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Sign Up",
        style = MaterialTheme.typography.headlineMedium,
        color = Color.White,
        fontWeight = FontWeight.W700,
        fontFamily = FontFamily.SansSerif
    )
}

@Composable
fun ErrorLoginMessage(state: CreateAccountState) {
    if (state.error?.isNotBlank() == true) {
        Text(
            text = state.error.toString(),
            color = Color.Red,
        )
    }
}

@Composable
fun LoginButton(viewModel: AuthViewModel, buttonEnabled: State<Boolean>, config: Configuration) {
    ElevatedButton(
        onClick = viewModel::onCreateAccount, shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        ), elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 14.dp,
            pressedElevation = 8.dp
        ),
        enabled = buttonEnabled.value,
        modifier = Modifier.fillMaxWidth(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.7f),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        Text(text = "Sign Up")
    }
}

@Composable
fun AuthBackgroundImage() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.topcircle),
            contentDescription = "Auth Bottom Start",
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(id = R.drawable.bottomcircle),
            contentDescription = "Auth Bottom End",
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.BottomEnd)
        )
    }
}