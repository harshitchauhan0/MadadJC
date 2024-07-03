package com.harshit.madad.home.di

import android.app.Application
import com.harshit.madad.home.data.repository.GuardianRepositoryIMPL
import com.harshit.madad.home.domain.repository.GuardianRepository
import com.harshit.madad.home.domain.use_cases.GetContactsUseCase
import com.harshit.madad.home.domain.use_cases.SaveGuardianListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GuardianModule {

    @Provides
    @Singleton
    fun providesSaveGuardianListUseCase(guardianRepository: GuardianRepository): SaveGuardianListUseCase {
        return SaveGuardianListUseCase(guardianRepository)
    }

    @Provides
    @Singleton
    fun provideGetContactsUseCase(guardianRepository: GuardianRepository): GetContactsUseCase {
        return GetContactsUseCase(guardianRepository)
    }
}