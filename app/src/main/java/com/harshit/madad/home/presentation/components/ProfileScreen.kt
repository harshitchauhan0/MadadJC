package com.harshit.madad.home.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(controller: NavHostController, viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.profileState.collectAsState()
    val nameState by viewModel.nameState.collectAsState()
    val emailState by viewModel.emailState.collectAsState()
    var isOpen by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isOpen = true
    }
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
                ScreenHeading(
                    onBackClick = { controller.navigateUp() },
                    heading = stringResource(id = R.string.profile_screen_heading)
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semiCircleBackground(color = lightGreen.copy(alpha = 0.5f)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileText("Name", isOpen)
                    ProfileTextField(
                        nameState.text,
                        viewModel::nameValueChange,
                        nameState.isEditing,
                        viewModel::toggleEditName
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ProfileText("Email", isOpen)
                    ProfileTextField(
                        emailState.text,
                        viewModel::emailValueChange,
                        emailState.isEditing,
                        viewModel::toggleEditEmail
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                ProfileButton("Continue", viewModel::updateNameAndEmail, isOpen)
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(state.guardians, key = { it.key }) { guardian ->
                var isVisible by remember { mutableStateOf(true) }
                AnimatedVisibility(
                    visible = isVisible,
                    exit = scaleOut(
                        targetScale = 0.5f,
                        animationSpec = tween(durationMillis = 500)
                    ) + fadeOut(animationSpec = tween(durationMillis = 500))
                ) {
                    GuardianItem(guardian, onRemoveGuardian = {
                        viewModel.removeGuardian(guardian)
                        isVisible = false
                    }, modifier = Modifier.animateItemPlacement())
                }
            }
        }
    }
    ProfileContinueButton(
        onAddGuardianClick = { controller.navigate(AppScreen.MainScreen.GuardianScreen.route) },
        isOpen
    )
}

/**
 * This is the extension to add a semi-circle background to the right side of the composable.
 *
 * @param color The color of the semi-circle background.
 * @param heightFactor The factor by which the height of the semi-circle background should be scaled.
 * @param widthFactor The factor by which the width of the semi-circle background should be scaled.
 *
 * @return The modified [Modifier] with a semi-circle background.
 * @author Harshit
 */
fun Modifier.semiCircleBackground(
    color: Color,
    heightFactor: Float = 1.5f,
    widthFactor: Float = 1.5f
) = this.drawBehind {
    val width = size.width
    val height = size.height
    val path = Path().apply {
        arcTo(
            rect = Rect(
                left = width / widthFactor,
                top = 0f,
                right = width * widthFactor,
                bottom = height * heightFactor
            ), startAngleDegrees = 0f, sweepAngleDegrees = 280f, forceMoveTo = false
        )
        lineTo(width, -200f)
        close()
    }

    drawPath(
        path = path, color = color
    )
}

@Composable
fun ProfileContinueButton(onAddGuardianClick: () -> Unit, isOpen: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp), contentAlignment = Alignment.BottomCenter
    ) {
        ProfileButton("Add Guardian", onAddGuardianClick, isOpen)
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
                .clip(RoundedCornerShape(8.dp))
                .shadow(elevation = 14.dp)
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
fun ProfileButton(text: String, onClick: () -> Unit, isOpen: Boolean) {
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
                this.scaleY = scale
            },
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
fun ProfileText(text: String, isOpen: Boolean) {
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
    val configuration = LocalConfiguration.current.orientation
    Text(
        modifier = Modifier
            .fillMaxSize(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.8f)
            .graphicsLayer {
                this.translationX = translateX
                this.alpha = alpha
                TransformOrigin(0f, 0f)
            },
        text = text, fontSize = 15.sp, style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun ProfileTextField(
    value: String, onValueChange: (String) -> Unit, isReadOnly: Boolean, onEditClick: () -> Unit
) {
    val configuration = LocalConfiguration.current.orientation
    OutlinedTextField(maxLines = 1,
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(if (configuration == Configuration.ORIENTATION_PORTRAIT) 1.0f else 0.8f)
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
fun ScreenHeading(
    onBackClick: () -> Unit,
    heading: String,
    modifier: Modifier = Modifier,
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    color: Color = Color.Black,
    showRefresh: Boolean = false,
    onRefreshClick: () -> Unit = {}
) {
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        isOpen = true
    }
    val translateY by animateFloatAsState(
        targetValue = if (isOpen) 0f else -100f,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backGroundColor)
            .graphicsLayer {
                this.translationY = translateY
            }
            .padding(bottom = 6.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onBackClick)
                .align(Alignment.TopStart),
            tint = color
        )

        Text(
            text = heading,
            fontSize = 22.sp,
            style = MyTypography.headlineMedium,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.TopCenter),
            textAlign = TextAlign.Center,
            color = color
        )
        if (showRefresh) {
            var rotateDegree by remember { mutableFloatStateOf(0f) }

            val rotate by animateFloatAsState(
                targetValue = rotateDegree,
                animationSpec = tween(durationMillis = 500), label = ""
            )

            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = rotate
                    }
                    .clip(CircleShape)
                    .clickable {
                        rotateDegree += 360f
                        onRefreshClick()
                    }
                    .align(Alignment.TopEnd),
                tint = color
            )
        }
    }
}