package com.harshit.madad.authentication.util

data class ForgetPasswordState (
    val isLoading:Boolean = false,
    val error:String? = null,
    val isForgetPassword:Boolean = false
)