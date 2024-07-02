package com.harshit.madad.home.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.authentication.presentation.components.LoadingIndicator
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.presentation.viewmodels.ProfileViewModel
import com.harshit.madad.ui.theme.darkViolet

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuardianScreen(
    controller: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
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
                .padding(bottom = 16.dp)
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

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun ContactListItem(
//    onLongClick: () -> Unit, onToggle: () -> Unit, item: ContactItem
//) {
//    Row(
//        modifier = Modifier
//            .combinedClickable(
//                onClick = {}, onLongClick = onLongClick
//            )
//            .fillMaxWidth()
//            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
//    ) {
//        ContactRadioSection(
//            item.isSelected, onToggle, Color.Blue
//        )
//        ContactSection(
//            modifier = Modifier.weight(1f), name = item.name, phoneNumber = item.phoneNumber
//        )
//        if (item.isSuperGuardian) {
//            SuperGuardianTextSection()
//        }
//    }
//}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListItem(
    onLongClick: () -> Unit,
    onToggle: () -> Unit,
    item: ContactItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            ContactRadioSection(
                selected = item.isSelected,
                onToggle = onToggle,
                colors = Color.Blue
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.phoneNumber,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption
                )
            }
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
            text = name, maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        Text(text = phoneNumber, maxLines = 1)
    }
}

@Composable
fun SuperGuardianTextSection(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
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
