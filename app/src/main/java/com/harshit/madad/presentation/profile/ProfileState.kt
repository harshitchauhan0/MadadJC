package com.harshit.madad.presentation.profile

import com.harshit.madad.domain.model.ContactItem

data class ProfileState(
    val guardians: List<ContactItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
)

data class EditTextState(
    val isEditing: Boolean = false,
    val text: String = "",
    val isEnabled: Boolean = true,
)