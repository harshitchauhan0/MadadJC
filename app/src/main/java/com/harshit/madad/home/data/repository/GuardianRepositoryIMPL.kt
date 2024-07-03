package com.harshit.madad.home.data.repository

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.madad.common.Constants
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.GuardianRepository
import kotlinx.coroutines.tasks.await

class GuardianRepositoryIMPL(private val application: Application) : GuardianRepository {
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override suspend fun saveGuardianList(guardianList: List<ContactItem>) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val userDocRef = database.collection("Users").document(userId)
        val batch = database.batch()
        guardianList.forEach { contactItem ->
            val docRef = userDocRef.collection("contacts").document(contactItem.id.toString())
            batch.set(docRef, contactItem)
        }
        if (guardianList.any { it.isSuperGuardian }) {
            val sharedPreferences = application.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putString(
                Constants.PHONE_KEY, guardianList.find { it.isSuperGuardian }?.phoneNumber
            ).apply()
        }
        batch.commit().await()
    }
}