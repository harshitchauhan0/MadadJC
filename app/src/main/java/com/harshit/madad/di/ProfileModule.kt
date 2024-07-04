package com.harshit.madad.di

import com.harshit.madad.domain.repository.ProfileRepository
import com.harshit.madad.domain.usecase.EmailUseCase
import com.harshit.madad.domain.usecase.GetGuardianListUseCase
import com.harshit.madad.domain.usecase.GetUserNameUseCase
import com.harshit.madad.domain.usecase.RemoveGuardianUseCase
import com.harshit.madad.domain.usecase.SaveNameUseCase
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

    @Provides
    @Singleton
    fun provideEmailUseCase(profileRepository: ProfileRepository): EmailUseCase {
        return EmailUseCase(profileRepository)
    }
}