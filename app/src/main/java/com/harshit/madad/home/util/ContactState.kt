package com.harshit.madad.home.util

import androidx.compose.runtime.mutableStateListOf
import com.harshit.madad.home.data.remote.dto.ContactItem

data class ContactState(
    val contactList: List<ContactItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
