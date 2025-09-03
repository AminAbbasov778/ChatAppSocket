package com.example.chatapp.core.data.mapper

import com.example.chatapp.chatscreen.data.SendMessage
import com.example.chatapp.core.data.models.GetMessage
import com.example.chatapp.core.data.models.UserData
import com.example.chatapp.core.domain.models.MessageModel
import com.example.chatapp.core.domain.models.UserModel

fun UserData.toDomain(): UserModel {
    return UserModel(
        id = this.id ?: "unknown_id",
        username = this.username ?: "unknown_user",
        photo = this.photo ?: "",
        isOnline = false
    )
}

fun GetMessage.toDomain(): MessageModel{
    return MessageModel(senderId,message,timestamp= timestamp)
}

fun MessageModel.toData(): SendMessage {
    return SendMessage(senderId, message, receiverId ?: "1")
}


