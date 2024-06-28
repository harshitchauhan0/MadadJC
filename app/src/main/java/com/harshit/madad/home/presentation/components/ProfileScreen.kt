package com.harshit.madad.home.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.common.AppScreen
import com.harshit.madad.home.data.remote.dto.Guardian
import com.harshit.madad.home.presentation.viewmodels.ProfileViewModel
import com.harshit.madad.ui.theme.MyTypography
import com.harshit.madad.ui.theme.darkGreen
import com.harshit.madad.ui.theme.lightGreen

val list = List(20) {
    Guardian(name = "$it. Bhondu", phoneNumber = "tel: $it 4546545")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(controller: NavHostController, viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val nameState by viewModel.nameState.collectAsState()
    val emailState by viewModel.emailState.collectAsState()
    val configuration = LocalConfiguration.current.orientation
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            stickyHeader {
                ProfileHeading(onBackClick = { controller.navigateUp() })
            }
            item {
                ProfileContent(name = nameState.text,
                    email = emailState.text,
                    isEditingName = nameState.isEditing,
                    isEditingEmail = emailState.isEditing,
                    onNameChange = viewModel::nameValueChange,
                    onEmailChange = viewModel::emailValueChange,
                    onEditNameClick = viewModel::toggleEditName,
                    onEditEmailClick = viewModel::toggleEditEmail,
                    configuration = configuration,
                    onContinueClick = viewModel::updateNameAndEmail)
            }
            items(state.guardians) { guardian ->
                var isVisible by remember { mutableStateOf(true) }
                AnimatedVisibility(visible = isVisible,
                    exit = slideOutHorizontally { -it } + fadeOut()) {
                    GuardianItem(guardian, onRemoveGuardian = {
                        viewModel.removeGuardian(guardian)
                        isVisible = false
                    }, modifier = Modifier.animateItemPlacement())
                }
            }
        }
    }
    ProfileContinueButton(onAddGuardianClick = { controller.navigate(AppScreen.MainScreen.GuardianScreen.route) })
}

@Composable
fun ProfileContent(
    configuration: Int,
    onContinueClick: () -> Unit,
    name: String,
    email: String,
    isEditingName: Boolean,
    isEditingEmail: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onEditNameClick: () -> Unit,
    onEditEmailClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.8f)) {
            ProfileText("Name")
            ProfileTextField(name, onNameChange, isEditingName, onEditNameClick)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileText("Email")
            ProfileTextField(email, onEmailChange, isEditingEmail, onEditEmailClick)
            Spacer(modifier = Modifier.height(12.dp))
        }
        ProfileButton("Continue", onContinueClick)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun ProfileContinueButton(onAddGuardianClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp), contentAlignment = Alignment.BottomCenter
    ) {
        ProfileButton("Add Guardian", onAddGuardianClick)
    }
}

@Composable
fun GuardianItem(guardian: Guardian, onRemoveGuardian: () -> Unit, modifier: Modifier) {
    val configuration = LocalConfiguration.current.orientation
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.8f)
                .background(color = lightGreen)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .padding(vertical = 14.dp)
            ) {
                Text(
                    text = guardian.name,
                    fontSize = 15.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .align(Alignment.Start)
                )
                Text(
                    text = guardian.phoneNumber,
                    fontSize = 12.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )

            }
            IconButton(onClick = onRemoveGuardian) {
                Icon(
                    painter = painterResource(id = R.drawable.gremove),
                    contentDescription = "Remove Guardian"
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun ProfileButton(text: String, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current.orientation
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = onClick,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.8f),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 12.dp, pressedElevation = 6.dp
            ),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = darkGreen, contentColor = Color.White
            )
        ) {
            Text(
                text = text, fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProfileText(text: String) {
    Text(
        text = text, fontSize = 15.sp, style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun ProfileTextField(
    value: String, onValueChange: (String) -> Unit, isReadOnly: Boolean, onEditClick: () -> Unit
) {
    OutlinedTextField(maxLines = 1,
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF5A8FAF),
            unfocusedBorderColor = Color(0xFFD5E8ED),
        ),
        readOnly = isReadOnly,
        trailingIcon = {
            Icon(imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier.clickable { onEditClick() })
        })
}

@Composable
fun ProfileHeading(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 6.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onBackClick)
                .align(Alignment.TopStart)
        )

        Text(
            text = "Profile",
            fontSize = 22.sp,
            style = MyTypography.headlineMedium,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.TopCenter),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun W() {
//    ProfileScreen()
}