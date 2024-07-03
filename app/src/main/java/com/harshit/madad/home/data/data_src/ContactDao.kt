package com.harshit.madad.home.data.data_src

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harshit.madad.home.data.remote.dto.ContactItem
import kotlinx.coroutines.flow.Flow


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