package com.harshit.madad.presentation.authentication.state

data class SignInState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val isSignedIn:Boolean = false
)
