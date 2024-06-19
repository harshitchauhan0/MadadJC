package com.harshit.madad.authentication.presentation.viewmodels

data class SignInState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val isSignedIn:Boolean = false
)
