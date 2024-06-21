package com.harshit.madad.home.util

data class CallState(
    val isLoading: Boolean = false,
    val error: String = "",
    val number: String = "",
    val isCalling: Boolean = false
)