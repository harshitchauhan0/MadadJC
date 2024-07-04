package com.harshit.madad.presentation.message

import com.harshit.madad.domain.model.ContactItem

data class MessageState (
    val error: String = "",
    val isLoading: Boolean = false,
    val guardians: List<ContactItem> = emptyList(),
    val superGuardianNumber: String = "",
    val onHelpClick: Boolean = false
)