package com.harshit.madad.home.util

import com.harshit.madad.home.data.remote.dto.ContactItem

data class MessageState (
    val error: String = "",
    val isLoading: Boolean = false,
    val guardians: List<ContactItem> = emptyList(),
    val superGuardianNumber: String = "",
    val onHelpClick: Boolean = false
)