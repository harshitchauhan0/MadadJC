package com.harshit.madad.presentation.home

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harshit.madad.R
import com.harshit.madad.presentation.authentication.components.ErrorMessage
import com.harshit.madad.presentation.authentication.components.LoadingIndicator
import com.harshit.madad.common.Constants
import com.harshit.madad.common.FeatureItem
import com.harshit.madad.common.featureList
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    controller: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    onCallClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    var isOpen by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isOpen = true
    }
    val callState by viewModel.callState.collectAsState()

//    This is if we are changing name in profile screen
    controller.currentBackStackEntry
        ?.savedStateHandle?.getStateFlow<Boolean?>(Constants.NAME_CHANGED, false)
        ?.collectAsState()?.value?.let {
            if (it) {
                viewModel.fetchUserInfo()
                controller.currentBackStackEntry?.savedStateHandle?.remove<Boolean>(Constants.NAME_CHANGED)
            }
        }


    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopBar(onLogout = {
            viewModel.onLogout()
            onLogout()
        }, name = callState.name, isOpen)
    }) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column {
                    HomeScreenTextBanner(isOpen = isOpen)
                    Spacer(modifier = Modifier.height(16.dp))
                    FeatureText(isOpen = isOpen)
                    if (callState.error.isNotBlank()) {
                        ErrorMessage(callState.error)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            items(count = featureList.size, key = { it }) {
                FeatureCard(featureList[it], isOpen, controller) {
                    onCallClick(callState.number)
                }
            }
        }
    }
    if (callState.isLoading) {
        LoadingIndicator()
    }
}

@Composable
fun FeatureCard(
    featureName: FeatureItem,
    isOpen: Boolean,
    controller: NavHostController,
    onCallClick: () -> Unit
) {
    val alpha = animateFloatAsState(
        targetValue = if (isOpen) 1.0f else 0.5f,
        animationSpec = tween(durationMillis = 1000),
        label = "Alpha"
    )
    val scale = animateFloatAsState(
        targetValue = if (isOpen) 1.0f else 0.7f,
        animationSpec = tween(durationMillis = 1000),
        label = "Scale"
    )
    val configuration = LocalConfiguration.current.orientation
    val maxWidth = if (configuration == Configuration.ORIENTATION_PORTRAIT) 0.9f else 0.6f
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(maxWidth)
                .aspectRatio(1f)
                .padding(start = 8.dp, top = 8.dp)
                .graphicsLayer {
                    this.scaleX = scale.value
                    this.scaleY = scale.value
                    this.alpha = alpha.value
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp,
                pressedElevation = 0.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(featureName.backGroundColor)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        if (featureName.route != null) {
                            controller.navigate(featureName.route)
                        } else {
                            onCallClick()
                        }
                    },
                contentAlignment = if (configuration == Configuration.ORIENTATION_PORTRAIT) Alignment.TopStart else Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 4.dp,
                        bottom = 4.dp
                    )
                ) {
                    Icon(
                        imageVector = featureName.imageVector!!,
                        contentDescription = featureName.contentDescription,
                        tint = featureName.iconTint
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = featureName.text,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.W700,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp,
                        fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                        color = Color.Black
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FeatureText(isOpen: Boolean) {
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
    Text(
        modifier = Modifier
            .offset(x = 50.dp)
            .graphicsLayer {
                this.translationX = translateX
                this.alpha = alpha
                TransformOrigin(0f, 0f)
            },
        text = "Features",
        color = Color.Black,
        fontWeight = FontWeight.W700,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun HomeScreenTextBanner(modifier: Modifier = Modifier, isOpen: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "Alpha"
    )
    val translateX by animateFloatAsState(
        targetValue = if (isOpen) 0f else 1000f,
        animationSpec = tween(durationMillis = 1000),
        label = "Translate X"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .graphicsLayer(
                transformOrigin = TransformOrigin(0f, 0f), alpha = alpha, translationX = translateX
            ), contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                .background(Color(0xFF771F54))
                .padding(start = 10.dp)
        ) {
            Text(
                text = "Hi,",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(top = 15.dp)
            )
            Text(
                text = "I am Madad",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "How can I help you?",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 15.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onLogout: () -> Unit, name: String, isOpen: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "Alpha"
    )
    val translateY by animateFloatAsState(
        targetValue = if (isOpen) 0f else -1000f,
        animationSpec = tween(durationMillis = 1000),
        label = "Translate Y"
    )
    TopAppBar(modifier = Modifier.graphicsLayer {
        this.translationY = translateY
        this.alpha = alpha
        TransformOrigin(0f, 0f)
    }, title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileImage()
            Spacer(modifier = Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.Center) {
                HelloNameText(name)
                DateText()
            }
        }
    }, actions = {
        LogOutMenu(onLogout)
    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun LogOutMenu(onLogout: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Menu",
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
        )
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(text = { Text("Logout") }, onClick = {
            expanded = false
            onLogout()
        })
    }
}

@Composable
fun HelloNameText(name: String) {
    Text(
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        text = "Hello, $name"
    )
}

@Composable
fun ProfileImage() {
    Image(
        painterResource(id = R.drawable.app_logo),
        contentDescription = "Profile Pic",
        modifier = Modifier
            .clip(CircleShape)
            .size(60.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun DateText() {
    val formattedDate = rememberSaveable {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())
        currentDate.format(formatter)
    }

    Text(
        text = formattedDate,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontFamily.SansSerif,
        color = Color.Gray,
        style = MaterialTheme.typography.bodyLarge
    )
}