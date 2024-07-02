package com.harshit.madad.home.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.madad.common.Constants
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.ProfileRepository
import kotlinx.coroutines.tasks.await

class ProfileRepositoryIMPL(private val application: Application): ProfileRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun saveName(name: String) {
        val sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Constants.NAME_KEY,name).apply()
    }

    override fun getName(): String {
        val sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(Constants.NAME_KEY, "")
        return name?:""
    }

    override fun getEmail(): String {
        return auth.currentUser?.email?: ""
    }

    override suspend fun removeGuardian(contactItem: ContactItem) {

    }

    override suspend fun guardianList(): List<ContactItem> {
        return emptyList()
    }
}