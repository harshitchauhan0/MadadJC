package com.harshit.madad.home.util

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val number: String = "",
    val onCallClick:()->Unit = {},
    val name: String = ""
)