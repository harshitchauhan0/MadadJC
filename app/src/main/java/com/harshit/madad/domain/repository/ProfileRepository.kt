package com.harshit.madad.domain.repository

import com.harshit.madad.domain.model.ContactItem

interface ProfileRepository {
    fun saveName(name: String)

    fun getName(): String

    fun getEmail(): String

    suspend fun removeGuardian(contactItem: ContactItem)

    suspend fun guardianList(): List<ContactItem>
}