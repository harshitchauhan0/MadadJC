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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harshit.madad.R
import com.harshit.madad.authentication.presentation.viewmodels.AuthViewModel
import com.harshit.madad.authentication.presentation.viewmodels.SignInState
import com.harshit.madad.ui.theme.PurpleGrey40
import com.harshit.madad.ui.theme.lightBlue

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
        ErrorMessage(state)
        Spacer(modifier = Modifier.height(if (config.orientation == Configuration.ORIENTATION_PORTRAIT) 64.dp else 24.dp))
        SignInButton(viewModel, buttonEnabled, config)
    }

    AuthBottomEndImage()

    if (state.isLoading) {
        LoadingIndicator()
    } else if (state.isSignedIn) {
        onNavigate()
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
fun ErrorMessage(state: SignInState) {
    if (state.error?.isNotBlank() == true) {
        Text(
            text = state.error.toString(),
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
        CircularProgressIndicator(color = Color.Green)
    }
}
