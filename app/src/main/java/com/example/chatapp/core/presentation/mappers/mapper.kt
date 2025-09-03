package com.example.chatapp.core.presentation.mappers

import com.example.chatapp.core.domain.models.MessageModel
import com.example.chatapp.core.domain.models.UserModel
import com.example.chatapp.core.presentation.models.MessageUi
import com.example.chatapp.core.presentation.models.UserUi

fun UserModel.toUi() = UserUi(
    id = id,
    username = username,
    photo = photo,
    isOnline = isOnline
)

fun MessageUi.toDomain() : MessageModel{
    return MessageModel(
        senderId = senderId,
        message = message,
        receiverId = receiverId,
        timestamp = timestamp
    )


}
fun UserUi.toDomain() = UserModel(
    id = id,
    username = username,
    photo = photo,
    isOnline = isOnline
)

fun MessageModel.toUi(currentUserId : String) : MessageUi {
    return MessageUi(senderId, message, timestamp, senderId == currentUserId, receiverId ?: "1")
}

