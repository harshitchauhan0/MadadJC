package com.harshit.madad.presentation.guardian

import com.harshit.madad.domain.model.ContactItem

data class GuardianDialogState(
    val isDialogOpen: Boolean = false,
    val contactItem: ContactItem? = null
)
