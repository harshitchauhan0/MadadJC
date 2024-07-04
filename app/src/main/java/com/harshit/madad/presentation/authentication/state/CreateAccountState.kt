package com.harshit.madad.presentation.authentication.state

data class CreateAccountState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val isAccountCreated:Boolean = false
)
