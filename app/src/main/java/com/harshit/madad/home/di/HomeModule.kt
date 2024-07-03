package com.harshit.madad.home.di

import android.app.Application
import android.util.Log
import com.harshit.madad.home.data.repository.HomeRepositoryIMPL
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
    fun providesHomeRepository(application: Application): HomeRepository {
        return HomeRepositoryIMPL(application)
    }
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