package com.harshit.madad.authentication.util

data class SignInState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val isSignedIn:Boolean = false
)
