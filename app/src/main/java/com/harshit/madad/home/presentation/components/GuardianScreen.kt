package com.harshit.madad.home.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.authentication.presentation.components.LoadingIndicator
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.presentation.viewmodels.GuardianViewModel
import com.harshit.madad.ui.theme.darkViolet

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuardianScreen(
    controller: NavHostController,
    viewModel: GuardianViewModel = hiltViewModel()
) {
    val state by viewModel.contactState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = darkViolet)
            .padding(top = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            stickyHeader {
                ScreenHeading(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkViolet)
                        .padding(horizontal = 16.dp),
                    color = Color.White,
                    backGroundColor = darkViolet,
                    onBackClick = { controller.navigateUp() },
                    heading = stringResource(id = R.string.guardian_screen_heading),
                    showRefresh = true,
                    onRefreshClick = viewModel::fetchContacts
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            darkViolet,
                            shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
                        )
                )
            }
            item {
                MessageHelpingText(
                    text = stringResource(id = R.string.guardian_helping_text),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(state.contactList.size) { i ->
                ContactListItem(onLongClick = {
                    viewModel.showGuardianDialog(
                        show = true, contactItem = state.contactList[i]
                    )
                }, onToggle = {
                    viewModel.toggleSelection(i)
                }, item = state.contactList[i]
                )
            }
        }
        if (state.error.isNotEmpty()) {
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }

        FloatingActionButton(
            onClick = viewModel::saveGuardianList,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp),
            containerColor = MaterialTheme.colors.background
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Save",
                tint = darkViolet
            )
        }
    }
    if (state.isLoading) {
        LoadingIndicator()
    }

    val superGuardianDialogState = viewModel.showGuardianDialog.collectAsState().value
    if (superGuardianDialogState.isDialogOpen) {
        SuperGuardianAlertDialog(onDismiss = {
            viewModel.showGuardianDialog(false)
        }, onConfirm = {
            viewModel.saveSuperGuardian(superGuardianDialogState.contactItem)
            viewModel.showGuardianDialog(false)
        }, contactItem = superGuardianDialogState.contactItem
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListItem(
    onLongClick: () -> Unit,
    onToggle: () -> Unit,
    item: ContactItem
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(darkViolet.copy(alpha = 0.0f), darkViolet.copy(alpha = 0.5f), darkViolet.copy(alpha = 0.1f))
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(gradient).padding(top = 12.dp, bottom = 12.dp, end = 16.dp)
        ) {
            ContactRadioSection(
                selected = item.isSelected or item.isSuperGuardian,
                onToggle = onToggle,
                colors = Color.Blue
            )
            ContactSection(
                name = item.name,
                phoneNumber = item.phoneNumber,
                modifier = Modifier.weight(1f)
            )
            if (item.isSuperGuardian) {
                Spacer(modifier = Modifier.width(16.dp))
                SuperGuardianTextSection()
            }
        }
    }
}

@Composable
fun SuperGuardianAlertDialog(
    onDismiss: () -> Unit, onConfirm: () -> Unit, contactItem: ContactItem?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Image(
                    painter = painterResource(id = R.drawable.super_guardian),
                    contentDescription = "Super Guardian"
                )
            },
            text = { Text("You want to make ${contactItem?.name} as super guardian") },
            confirmButton = {
                Button(
                    onClick = onConfirm
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text("No")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun ContactRadioSection(selected: Boolean, onToggle: () -> Unit, colors: Color) {
    RadioButton(
        selected = selected,
        onClick = onToggle,
        colors = RadioButtonDefaults.colors(selectedColor = colors)
    )
}

@Composable
fun ContactSection(modifier: Modifier = Modifier, name: String, phoneNumber: String) {
    Spacer(modifier = Modifier.width(16.dp))
    Column(modifier = modifier) {
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = phoneNumber,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.W400
        )
    }
}

@Composable
fun SuperGuardianTextSection() {
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        isOpen = true
    }
    val scaleX by animateFloatAsState(targetValue = if (isOpen) 1f else 0f, label = "Scale X")
    Box(
        modifier = Modifier
            .graphicsLayer {
                this.scaleX = scaleX
            }
            .background(
                color = Color.Green,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "Super",
            color = Color.White,
            style = MaterialTheme.typography.body2
        )
    }
}