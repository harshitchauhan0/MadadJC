package com.harshit.madad.data.repository

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.harshit.madad.common.Constants
import com.harshit.madad.data.local.ContactDao
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.domain.repository.GuardianRepository

class GuardianRepositoryIMPL(private val application: Application, private val dao: ContactDao) :
    GuardianRepository {
    override suspend fun saveGuardianList(guardianList: List<ContactItem>) {
        if (guardianList.any { it.isSuperGuardian }) {
            val sharedPreferences = application.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putString(
                Constants.PHONE_KEY, guardianList.find { it.isSuperGuardian }?.phoneNumber
            ).apply()
        }
        dao.insertContacts(guardianList)
    }

    override suspend fun getContacts(): List<ContactItem> {
        val cr: ContentResolver = application.contentResolver
        val cursor: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        val mobileNoSet = HashSet<String>()
        val contacts = mutableListOf<ContactItem>()
        cursor?.use {
            val nameIndex: Int = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val numberIndex: Int = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val idIndex: Int = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

            var id: String
            var name: String
            var number: String
            while (it.moveToNext()) {
                id = it.getString(idIndex)
                name = it.getString(nameIndex)
                number = it.getString(numberIndex)
                number = number.replace(" ", "")
                if (!mobileNoSet.contains(number)) {
                    contacts.add(ContactItem(name = name, phoneNumber = number, id = id.toInt()))
                    mobileNoSet.add(number)
                }
            }
        }
        return contacts
    }

    companion object {
        private val PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
    }
}