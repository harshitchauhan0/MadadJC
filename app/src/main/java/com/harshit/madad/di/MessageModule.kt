package com.harshit.madad.di

import com.harshit.madad.domain.repository.MessageRepository
import com.harshit.madad.domain.usecase.GetHelperGuardianListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MessageModule {

    @Provides
    @Singleton
    fun providesGetGuardianListUseCase(messageRepository: MessageRepository): GetHelperGuardianListUseCase {
        return GetHelperGuardianListUseCase(messageRepository)
    }
}