package com.harshit.madad.home.di

import com.harshit.madad.home.domain.repository.HomeRepository
import com.harshit.madad.home.domain.use_cases.LogOutUseCase
import com.harshit.madad.home.domain.use_cases.UserInfoUseCase
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