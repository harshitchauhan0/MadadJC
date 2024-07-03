package com.harshit.madad.home.data.repository

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
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
                    contacts.add(ContactItem(name = name, phoneNumber =  number, id = id.toInt()))
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