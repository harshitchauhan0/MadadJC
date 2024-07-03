package com.harshit.madad.home.di

import android.app.Application
import com.harshit.madad.home.data.repository.MessageRepositoryIMPL
import com.harshit.madad.home.domain.repository.MessageRepository
import com.harshit.madad.home.domain.use_cases.GetGuardianListUseCase
import com.harshit.madad.home.domain.use_cases.GetHelperGuardianListUseCase
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