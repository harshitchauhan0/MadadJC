package com.harshit.madad.di

import com.harshit.madad.domain.repository.GuardianRepository
import com.harshit.madad.domain.usecase.GetContactsUseCase
import com.harshit.madad.domain.usecase.SaveGuardianListUseCase
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