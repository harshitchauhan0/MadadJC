package com.harshit.madad.home.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuardianScreen(modifier: Modifier = Modifier) {
    LazyColumn {
        stickyHeader {

        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun W() {
    GuardianScreen()
}