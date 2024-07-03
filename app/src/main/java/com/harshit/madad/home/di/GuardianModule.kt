package com.harshit.madad.home.di

import android.app.Application
import com.harshit.madad.home.data.repository.GuardianRepositoryIMPL
import com.harshit.madad.home.domain.repository.GuardianRepository
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
    fun providesGuardianRepository(application: Application): GuardianRepository {
        return GuardianRepositoryIMPL(application)
    }

    @Provides
    @Singleton
    fun providesSaveGuardianListUseCase(guardianRepository: GuardianRepository): SaveGuardianListUseCase {
        return SaveGuardianListUseCase(guardianRepository)
    }
}