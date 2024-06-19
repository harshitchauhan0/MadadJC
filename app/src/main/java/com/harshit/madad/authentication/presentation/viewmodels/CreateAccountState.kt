package com.harshit.madad.authentication.presentation.viewmodels

data class CreateAccountState(
    val isLoading:Boolean = false,
    val error:String? = null,
    val isAccountCreated:Boolean = false
)
