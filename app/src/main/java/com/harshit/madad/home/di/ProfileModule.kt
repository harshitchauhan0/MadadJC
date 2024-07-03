package com.harshit.madad.home.di

import android.app.Application
import com.harshit.madad.home.data.repository.ProfileRepositoryIMPL
import com.harshit.madad.home.domain.repository.ProfileRepository
import com.harshit.madad.home.domain.use_cases.EmailUseCase
import com.harshit.madad.home.domain.use_cases.GetGuardianListUseCase
import com.harshit.madad.home.domain.use_cases.GetUserNameUseCase
import com.harshit.madad.home.domain.use_cases.RemoveGuardianUseCase
import com.harshit.madad.home.domain.use_cases.SaveNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun providesSaveNameUseCase(profileRepository: ProfileRepository): SaveNameUseCase {
        return SaveNameUseCase(profileRepository)
    }

    @Provides
    @Singleton
    fun providesGetGuardianListUseCase(profileRepository: ProfileRepository): GetGuardianListUseCase {
        return GetGuardianListUseCase(profileRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserNameUseCase(profileRepository: ProfileRepository): GetUserNameUseCase {
        return GetUserNameUseCase(profileRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveGuardianUseCase(profileRepository: ProfileRepository): RemoveGuardianUseCase {
        return RemoveGuardianUseCase(profileRepository)
    }

    fun provideEmailUseCase(profileRepository: ProfileRepository): EmailUseCase {
        return EmailUseCase(profileRepository)
    }
}