package com.harshit.madad.home.util

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val number: String = "",
    val name: String = ""
)