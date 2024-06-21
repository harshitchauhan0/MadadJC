package com.harshit.madad.authentication.util

data class CreateAccountState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val isAccountCreated:Boolean = false
)
