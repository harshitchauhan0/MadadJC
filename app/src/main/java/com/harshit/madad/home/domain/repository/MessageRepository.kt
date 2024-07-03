package com.harshit.madad.home.domain.repository

import com.harshit.madad.home.data.remote.dto.ContactItem
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun guardianList(): List<ContactItem>
}