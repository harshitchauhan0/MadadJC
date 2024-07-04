package com.harshit.madad.domain.repository

import com.harshit.madad.domain.model.ContactItem

interface GuardianRepository {
    suspend fun saveGuardianList(guardianList: List<ContactItem>)

    suspend fun getContacts():List<ContactItem>
}