package com.harshit.madad.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harshit.madad.domain.model.ContactItem


@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactItem>)

    @Query("SELECT * FROM contactitem")
    suspend fun getAllContacts(): List<ContactItem>

    @Query("DELETE FROM contactitem")
    suspend fun deleteAllContacts()
}