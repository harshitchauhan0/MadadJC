package com.harshit.madad.data.repository

import android.app.Application
import android.content.Context
import com.harshit.madad.common.Constants
import com.harshit.madad.data.local.ContactDao
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.domain.repository.ProfileRepository

class ProfileRepositoryIMPL(private val application: Application, private val dao: ContactDao) :
    ProfileRepository {
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
        val sharedPreferences =
            application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.EMAIL_KEY, "") ?: ""
    }

    override suspend fun removeGuardian(contactItem: ContactItem) {
        val item = contactItem.copy(isSelected = false, isSuperGuardian = false)
        dao.insertContact(item)
    }

    override suspend fun guardianList(): List<ContactItem> {
        return dao.getAllContacts()
    }
}