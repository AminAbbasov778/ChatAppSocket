package com.example.chatapp.core.data.event

import com.example.chatapp.core.data.models.UserData

sealed class SocketEvent {
    data class RegisterUser(val userId: String) : SocketEvent()
    data class UserList(val users: List<UserData>) : SocketEvent()
    data class UserOnline(val userId: String) : SocketEvent()
    data class UserOffline(val userId: String) : SocketEvent()
}