package com.harshit.madad.home.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.MessageRepository
import kotlinx.coroutines.tasks.await

class MessageRepositoryIMPL :MessageRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    override suspend fun guardianList(): List<ContactItem> {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val result = mutableListOf<ContactItem>()
        val documents = database.collection("Users").document(userId).collection("contacts")
            .get()
            .await()

        for (document in documents) {
            val contactItem = document.toObject(ContactItem::class.java)
            result.add(contactItem)
        }
        return result
    }
}