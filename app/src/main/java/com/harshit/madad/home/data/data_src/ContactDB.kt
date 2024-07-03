package com.harshit.madad.home.data.data_src

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harshit.madad.home.data.remote.dto.ContactItem

@Database(entities = [ContactItem::class], version = 1, exportSchema = false)
abstract class ContactDB: RoomDatabase() {
    abstract val contactDao: ContactDao
}