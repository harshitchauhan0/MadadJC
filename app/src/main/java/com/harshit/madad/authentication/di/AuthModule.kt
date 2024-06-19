package com.harshit.madad.authentication.di

import com.harshit.madad.authentication.data.repository.AuthRepositoryIMPL
import com.harshit.madad.authentication.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun providesAuthRepository(): AuthRepository{
        return AuthRepositoryIMPL()
    }
}