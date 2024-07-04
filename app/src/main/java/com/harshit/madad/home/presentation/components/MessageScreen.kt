package com.harshit.madad.home.presentation.components

import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.authentication.presentation.components.LoadingIndicator
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.presentation.viewmodels.MessageViewModel
import com.harshit.madad.ui.theme.darkPurple
import com.harshit.madad.ui.theme.lightPurple

@Composable
fun MessageScreen(
    controller: NavHostController,
    onHelpClick: (isCallingSelected: Boolean, superGuardianNumber: String, guardianList: List<ContactItem>, message: String) -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isOpen by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isOpen = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkPurple),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeading(
            backGroundColor = darkPurple,
            color = Color.White,
            onBackClick = { controller.navigateUp() },
            heading = stringResource(id = R.string.message_screen_heading),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
        )
        MessageContent(modifier = Modifier.weight(1f))
    }

    if (state.isLoading) {
        LoadingIndicator()
    }
    if (state.onHelpClick) {
        if (viewModel.superGuardianSelected.value) {
            onHelpClick(true, state.superGuardianNumber, state.guardians, viewModel.message.value)
        } else {
            onHelpClick(false, "", state.guardians, viewModel.message.value)
        }
    }
}

@Composable
fun SuperGuardianSection(viewModel: MessageViewModel = hiltViewModel()) {
    var isOpen by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        isOpen = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "Alpha"
    )
    val translateY by animateFloatAsState(
        targetValue = if (isOpen) 0f else -100f,
        animationSpec = tween(durationMillis = 1000),
        label = "Translate Y"
    )
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.graphicsLayer {
        this.alpha = alpha
        this.translationY = translateY
    }) {
        RadioButton(
            selected = viewModel.superGuardianSelected.value,
            onClick = { viewModel.onSuperGuardianSelected(!viewModel.superGuardianSelected.value) },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF771F54))
        )
        Text(
            text = "Super Guardian", fontSize = 15.sp
        )
    }
}

@Composable
fun MessageContent(modifier: Modifier = Modifier, viewModel: MessageViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current.orientation
    Column(
        modifier = modifier
            .fillMaxWidth(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.85f)
            .fillMaxHeight()
            .background(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp), color = Color.White
            )
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        MessageHelpingText(
            text = stringResource(id = R.string.message_screen_info)
        )
        Spacer(modifier = Modifier.height(16.dp))
        RememberMessageTextField()
        Spacer(modifier = Modifier.height(16.dp))
        HelpButton(onHelpClick = viewModel::loadGuardians)
        Spacer(modifier = Modifier.height(16.dp))
        SuperGuardianSection()
        MessageHelpingText(
            text = stringResource(id = R.string.super_guardian_text)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RememberMessageTextField(viewModel: MessageViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current.orientation

    OutlinedTextField(
        shape = RoundedCornerShape(20.dp),
        value = viewModel.message.value,
        onValueChange = viewModel::updateMessage,
        modifier = Modifier
            .fillMaxWidth(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.8f)
            .defaultMinSize(minHeight = 200.dp)
            .fillMaxHeight(0.3f)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = lightPurple,
            focusedBorderColor = darkPurple,
            focusedContainerColor = Color.White
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            viewModel.loadGuardians()
        }),
        placeholder = {
            Text(
                text = stringResource(id = R.string.message_placeholder), color = Color.Gray
            )
        },
    )
}

@Composable
fun MessageHelpingText(modifier: Modifier = Modifier, text: String) {
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        isOpen = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "Alpha"
    )
    val translateX by animateFloatAsState(
        targetValue = if (isOpen) 0f else -1000f,
        animationSpec = tween(durationMillis = 1000),
        label = "Translate X"
    )
    Text(text = text,
        fontSize = 13.sp,
        color = Color(0xFF3C3C3C),
        lineHeight = 19.sp,
        modifier = modifier
            .padding(top = 14.dp)
            .graphicsLayer {
                this.translationX = translateX
                this.alpha = alpha
                TransformOrigin(0f, 0f)
            })
}

@Composable
fun HelpButton(onHelpClick: () -> Unit) {
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        isOpen = true
    }
    val context = LocalContext.current
    val messagePermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it) {
                onHelpClick()
            }
        }
    val alpha by animateFloatAsState(
        targetValue = if (isOpen) 1.0f else 0.5f,
        animationSpec = tween(durationMillis = 1000),
        label = "Alpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (isOpen) 1.0f else 0.6f,
        animationSpec = tween(durationMillis = 1000),
        label = "Scale"
    )
    val configuration = LocalConfiguration.current.orientation
    ElevatedButton(
        onClick = {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onHelpClick()
            } else {
                messagePermission.launch(android.Manifest.permission.SEND_SMS)
            }
        },
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.8f)
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
            },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 12.dp, pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = darkPurple, contentColor = Color.White
        ),
    ) {
        Text(
            text = "Help", fontSize = 14.sp
        )
    }
}
