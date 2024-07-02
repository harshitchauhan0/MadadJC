package com.harshit.madad.home.util

import com.harshit.madad.home.data.remote.dto.ContactItem

data class ProfileState(
    val guardians: List<ContactItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
)

data class EditTextState(
    val isEditing: Boolean = false,
    val text: String = "",
)