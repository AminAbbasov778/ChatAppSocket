package com.example.chatapp.core.data.network

import android.util.Log
import com.example.chatapp.core.data.models.GetMessage
import com.example.chatapp.chatscreen.data.SendMessage
import com.example.chatapp.core.data.event.SocketEvent
import com.example.chatapp.core.data.models.UserData
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.get

@Singleton
class SocketHandler @Inject constructor(
    private val socket: Socket,
) {
    private val gson = Gson()

    private val _eventsFlow = MutableStateFlow<SocketEvent>(SocketEvent.UserOnline(""))
    val eventsFlow: StateFlow<SocketEvent> = _eventsFlow

    private val _messagesFlow = MutableSharedFlow<GetMessage>(replay = 0, extraBufferCapacity = 10)
    val messagesFlow: Flow<GetMessage> = _messagesFlow.asSharedFlow()

    fun connectSocket(currentUserId: String) {
        socket.connect()
        socket.off("receive_message")
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit("register_user", currentUserId)
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
        }

        socket.on(Socket.EVENT_DISCONNECT) { args ->
        }

        socket.on("user_list") { args ->
            val jsonString = args[0].toString()
            val users = try {
                gson.fromJson(jsonString, Array<UserData>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
            _eventsFlow.tryEmit(SocketEvent.UserList(users))
        }

        socket.on("user_online") { args ->
            try {
                val jsonString = args[0].toString()           // args[0] JSON string olaraq gəlir
                val jsonMap = gson.fromJson(jsonString, Map::class.java)
                val userId = jsonMap["userId"] as? String
                if (userId != null) CoroutineScope(Dispatchers.IO).launch {
                    _eventsFlow.emit(SocketEvent.UserOnline(userId))
                }
            } catch (e: Exception) {
            }
        }

        socket.on("user_offline") { args ->

            try {
                val jsonString = args[0].toString()
                val jsonMap = gson.fromJson(jsonString, Map::class.java)
                val userId = jsonMap["userId"] as? String
                if (userId != null) CoroutineScope(Dispatchers.IO).launch {
                    _eventsFlow.emit(SocketEvent.UserOffline(userId))
                }
            } catch (e: Exception) {
            }
        }



        socket.on("receive_message") { args ->

            try {
                val json = args[0].toString()
                val msg = gson.fromJson(json, GetMessage::class.java)
                _messagesFlow.tryEmit(msg)
            } catch (e: Exception) {
            }
        }



    }

    fun enterChat(userId: String) {

        socket.emit("enter_chat", userId)
    }


    fun sendMessage(sendMessage: SendMessage) {


        val messageData = JSONObject()

        messageData.put("senderId", sendMessage.senderId)
        messageData.put("receiverId", sendMessage.receiverId)
        messageData.put("message", sendMessage.message)


        socket.emit("send_message", messageData)


    }
}
