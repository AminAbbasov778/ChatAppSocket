package com.example.chatapp.core.data.repository

import com.example.chatapp.core.data.network.SocketHandler
import com.example.chatapp.core.data.models.LastMessageInfo
import com.example.chatapp.core.data.event.SocketEvent
import com.example.chatapp.core.data.mapper.toData
import com.example.chatapp.core.data.mapper.toDomain
import com.example.chatapp.core.data.models.GetMessage
import com.example.chatapp.core.domain.models.MessageModel
import com.example.chatapp.core.domain.models.UserModel
import com.example.chatapp.core.domain.interfaces.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val socketHandler: SocketHandler
) : UserRepository {

    private val currentUsers = mutableMapOf<String, UserModel>()

    private val _lastMessageInfo = MutableStateFlow<List<LastMessageInfo>>(emptyList())
    val lastMessageInfo: StateFlow<List<LastMessageInfo>> = _lastMessageInfo


    override fun connect(currentUserId: String) {
        socketHandler.connectSocket(currentUserId)
    }
    override fun observeLastMessages(): StateFlow<List<LastMessageInfo>> {
        return lastMessageInfo
    }


    override fun setLastMessageInfo(userId: String, message: String, timestamp: String) {
        _lastMessageInfo.update { currentList ->
            val mutable = currentList.toMutableList()
            val index = mutable.indexOfFirst { it.userId == userId }
            if (index >= 0) {
                mutable[index] = LastMessageInfo(userId, message, timestamp)
            } else {
                mutable.add(LastMessageInfo(userId, message, timestamp))
            }
            mutable
        }
    }

    override fun observeUsers(): Flow<UserModel> {
        return socketHandler.eventsFlow.map { event ->
            when (event) {
                is SocketEvent.UserList -> {
                    currentUsers.clear()
                    event.users.forEach { user ->
                        currentUsers[user.id ?: "1"] = user.toDomain()
                    }
                }
                is SocketEvent.UserOnline -> {
                    currentUsers[event.userId]?.let {
                        currentUsers[event.userId] = it.copy(isOnline = true)
                    }
                }
                is SocketEvent.UserOffline -> {
                    currentUsers[event.userId]?.let {
                        currentUsers[event.userId] = it.copy(isOnline = false)
                    }
                }

                is SocketEvent.RegisterUser -> TODO()
            }
            currentUsers.values.toList()
        }.flatMapConcat { flowOf(*it.toTypedArray()) }
    }

    override fun observeMessages(): Flow<MessageModel> =
        socketHandler.messagesFlow.map { msg ->
            val domainMsg = msg.toDomain()
            setLastMessageInfo(domainMsg.senderId, domainMsg.message, domainMsg.timestamp ?: "")
            domainMsg
        }

    override fun sendMessage(message: MessageModel) {
        socketHandler.sendMessage(message.toData())
        setLastMessageInfo(message.receiverId ?: "1" , message.message, message.timestamp ?: "")
    }

    override fun enterChat(userId: String) {
        socketHandler.enterChat(userId)
    }
}