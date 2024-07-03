package com.harshit.madad.home.domain.repository

import com.harshit.madad.home.data.remote.dto.ContactItem
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun saveName(name: String)

    fun getName(): String

    fun getEmail(): String

    suspend fun removeGuardian(contactItem: ContactItem)

    suspend fun guardianList(): List<ContactItem>
}