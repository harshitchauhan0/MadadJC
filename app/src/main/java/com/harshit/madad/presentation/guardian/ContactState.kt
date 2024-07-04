package com.harshit.madad.presentation.guardian

import com.harshit.madad.domain.model.ContactItem

data class ContactState(
    val contactList: List<ContactItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
