package com.harshit.madad.home.util

import com.harshit.madad.home.data.remote.dto.Guardian

data class MessageState (
    val error: String = "",
    val isLoading: Boolean = false,
    val guardians: List<Guardian> = emptyList(),
    val superGuardianNumber: String = "",
    val onHelpClick: Boolean = false
)