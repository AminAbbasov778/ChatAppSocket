package com.example.chatapp.core.domain.models

data class MessageModel(
    val senderId: String,
    val message: String,
  val receiverId : String? = null,
    val timestamp: String?  = null
)
