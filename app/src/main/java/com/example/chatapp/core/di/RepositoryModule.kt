package com.example.chatapp.core.di

import com.example.chatapp.core.data.repository.UserRepositoryImpl
import com.example.chatapp.core.data.network.SocketHandler
import com.example.chatapp.core.domain.interfaces.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(socketHandler: SocketHandler): UserRepository  = UserRepositoryImpl(socketHandler)


}