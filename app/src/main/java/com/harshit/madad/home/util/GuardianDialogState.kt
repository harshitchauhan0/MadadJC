package com.harshit.madad.home.util

import com.harshit.madad.home.data.remote.dto.ContactItem

data class GuardianDialogState(
    val isDialogOpen: Boolean = false,
    val contactItem: ContactItem? = null
)
