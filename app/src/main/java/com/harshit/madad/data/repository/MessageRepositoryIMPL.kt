package com.harshit.madad.data.repository

import com.harshit.madad.data.local.ContactDao
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.domain.repository.MessageRepository

class MessageRepositoryIMPL(private val dao: ContactDao) : MessageRepository {
    override suspend fun guardianList(): List<ContactItem> {
        return dao.getAllContacts()
    }
}