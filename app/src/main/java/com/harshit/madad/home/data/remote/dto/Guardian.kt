package com.harshit.madad.home.data.remote.dto

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class Guardian(
    val id: Int?=null,
    val name: String = "",
    val phoneNumber: String = "",
    val isSuperGuardian: Boolean = false,
    val key: String = UUID.randomUUID().toString()
)
