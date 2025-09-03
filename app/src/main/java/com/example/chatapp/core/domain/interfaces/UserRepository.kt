package com.example.chatapp.core.domain.interfaces

import com.example.chatapp.core.data.models.LastMessageInfo
import com.example.chatapp.core.domain.models.MessageModel
import com.example.chatapp.core.domain.models.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    fun connect(currentUserId: String)
    fun observeUsers(): Flow<UserModel>
    fun sendMessage(message: MessageModel)
    fun enterChat(userId: String)
    fun observeMessages(): Flow<MessageModel>
    fun setLastMessageInfo(userId: String, message: String, timestamp: String)
    fun observeLastMessages(): StateFlow<List<LastMessageInfo>>
}