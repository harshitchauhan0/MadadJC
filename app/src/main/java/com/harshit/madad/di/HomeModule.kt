package com.harshit.madad.di

import com.harshit.madad.domain.repository.HomeRepository
import com.harshit.madad.domain.usecase.LogOutUseCase
import com.harshit.madad.domain.usecase.UserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun providesLogOutUseCase(homeRepository: HomeRepository): LogOutUseCase {
        return LogOutUseCase(homeRepository)
    }

    @Provides
    @Singleton
    fun providesUserInfoUseCase(homeRepository: HomeRepository): UserInfoUseCase {
        return UserInfoUseCase(homeRepository)
    }
}