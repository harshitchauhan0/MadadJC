package com.harshit.madad.authentication.presentation.viewmodels

data class ForgetPasswordState (
    val isLoading:Boolean = false,
    val error:String? = null,
    val isForgetPassword:Boolean = false
)