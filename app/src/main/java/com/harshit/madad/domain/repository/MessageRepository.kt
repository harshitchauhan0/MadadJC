package com.harshit.madad.domain.repository

import com.harshit.madad.domain.model.ContactItem

interface MessageRepository {
    suspend fun guardianList(): List<ContactItem>
}