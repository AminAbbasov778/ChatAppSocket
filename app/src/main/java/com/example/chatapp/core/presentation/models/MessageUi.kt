package com.example.chatapp.core.presentation.models

data class MessageUi(
    val senderId: String,
    val message: String,
    val timestamp: String? = null,
    val isMine: Boolean,
    val receiverId: String,
    var isTimeVisible: Boolean = false,
) {
}