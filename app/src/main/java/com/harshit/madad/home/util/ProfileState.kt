package com.harshit.madad.home.util

import com.harshit.madad.home.data.remote.dto.Guardian

data class ProfileState (
    val guardians: List<Guardian> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
)

data class EditTextState(
    val isEditing: Boolean = false,
    val text: String = "",
)