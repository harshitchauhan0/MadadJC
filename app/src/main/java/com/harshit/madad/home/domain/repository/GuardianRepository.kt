package com.harshit.madad.home.domain.repository

import com.harshit.madad.home.data.remote.dto.ContactItem

interface GuardianRepository {
    suspend fun saveGuardianList(guardianList: List<ContactItem>)
}