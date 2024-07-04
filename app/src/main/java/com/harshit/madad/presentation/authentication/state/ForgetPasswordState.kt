package com.harshit.madad.presentation.authentication.state

data class ForgetPasswordState (
    val isLoading:Boolean = false,
    val error:String? = null,
    val isForgetPassword:Boolean = false
)