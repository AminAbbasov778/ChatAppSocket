package com.example.chatapp.chatscreen.presenation

import com.example.chatapp.core.presentation.models.MessageUi


sealed class ChatEvent {
    data class SendMessage(val message: String,val recieverId: String) : ChatEvent()
    data class EnterChat(val userId: String) : ChatEvent()
    data class MessageReceived(val message: MessageUi) : ChatEvent()
    data class UpdateText(val text: String) : ChatEvent()
    data object ClearState : ChatEvent()
    data class ObserveIncomingMessages(val recieverId: String) : ChatEvent()
}