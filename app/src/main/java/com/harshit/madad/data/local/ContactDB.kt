package com.harshit.madad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harshit.madad.domain.model.ContactItem

@Database(entities = [ContactItem::class], version = 1, exportSchema = false)
abstract class ContactDB: RoomDatabase() {
    abstract val contactDao: ContactDao
}