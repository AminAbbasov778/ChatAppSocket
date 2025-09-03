package com.example.chatapp.core.presentation.models

data class UserUi(
    val id: String,
    val username: String,
    val photo: String,
    val isOnline: Boolean,
    val lastMessage: String? = null,
    val lastTime: String? = null,
    val messageCount: Int = 0

)