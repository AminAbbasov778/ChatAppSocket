package com.example.chatapp.core.di
import com.example.chatapp.core.data.network.SocketHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Provides
    @Singleton
    fun provideWebSocketClient(): Socket = IO.socket(
        "http://172.20.11.81:3000",
        IO.Options.builder().apply {
            setTransports(arrayOf("websocket"))
            setUpgrade(true)
            setRememberUpgrade(true)
            setForceNew(true)
            setReconnection(true)
        }.build()
    )

    @Provides
    @Singleton
    fun provideSocketHandler(socket: Socket): SocketHandler {
        return SocketHandler(socket)
    }
}
