package com.example.chatapp.chatscreen.presenation

import com.example.chatapp.core.presentation.models.MessageUi
import com.example.chatapp.core.presentation.models.UserUi

data class ChatState(
    val messages: List<MessageUi> = emptyList(),
    val messageText: String = "",
    val sender : UserUi? = null
)