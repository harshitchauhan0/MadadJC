package com.harshit.madad.home.di

import android.app.Application
import androidx.room.Room
import com.harshit.madad.home.data.data_src.ContactDB
import com.harshit.madad.home.data.repository.GuardianRepositoryIMPL
import com.harshit.madad.home.data.repository.HomeRepositoryIMPL
import com.harshit.madad.home.data.repository.MessageRepositoryIMPL
import com.harshit.madad.home.data.repository.ProfileRepositoryIMPL
import com.harshit.madad.home.domain.repository.GuardianRepository
import com.harshit.madad.home.domain.repository.HomeRepository
import com.harshit.madad.home.domain.repository.MessageRepository
import com.harshit.madad.home.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): ContactDB {
        return Room.databaseBuilder(application, ContactDB::class.java, "note_db").build()
    }

    @Provides
    @Singleton
    fun providesGuardianRepository(
        application: Application,
        contactDB: ContactDB
    ): GuardianRepository {
        return GuardianRepositoryIMPL(application, contactDB.contactDao)
    }

    @Provides
    @Singleton
    fun providesHomeRepository(application: Application): HomeRepository {
        return HomeRepositoryIMPL(application)
    }

    @Provides
    @Singleton
    fun providesMessageRepository(contactDB: ContactDB): MessageRepository {
        return MessageRepositoryIMPL(contactDB.contactDao)
    }

    @Provides
    @Singleton
    fun providesProfileRepository(
        application: Application,
        contactDB: ContactDB
    ): ProfileRepository {
        return ProfileRepositoryIMPL(application, contactDB.contactDao)
    }


}