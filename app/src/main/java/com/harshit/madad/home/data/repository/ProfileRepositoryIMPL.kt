package com.harshit.madad.home.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.madad.common.Constants
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.ProfileRepository
import kotlinx.coroutines.tasks.await

class ProfileRepositoryIMPL(private val application: Application) : ProfileRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    override fun saveName(name: String) {
        val sharedPreferences =
            application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Constants.NAME_KEY, name).apply()
    }

    override fun getName(): String {
        val sharedPreferences =
            application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(Constants.NAME_KEY, "")
        return name ?: ""
    }

    override fun getEmail(): String {
        return auth.currentUser?.email ?: ""
    }

    override suspend fun removeGuardian(contactItem: ContactItem) {
        Log.v("TAG", "removeGuardian: $contactItem")
        val item = contactItem.copy(isSelected = false)
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        database.collection("Users").document(userId)
            .collection("contacts")
            .document(contactItem.id.toString())
            .set(item)
            .await()
    }

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