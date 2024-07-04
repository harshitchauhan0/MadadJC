package com.harshit.madad.presentation.home

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val number: String = "",
    val name: String = ""
)