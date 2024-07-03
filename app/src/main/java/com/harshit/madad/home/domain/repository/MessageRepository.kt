package com.harshit.madad.home.domain.repository

import com.harshit.madad.home.data.remote.dto.ContactItem

interface MessageRepository {
    suspend fun guardianList(): List<ContactItem>
}