package com.harshit.madad.home.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.madad.home.data.data_src.ContactDao
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class MessageRepositoryIMPL(private val dao: ContactDao) :MessageRepository {
    override suspend fun guardianList(): List<ContactItem> {
        return dao.getAllContacts()
    }
}