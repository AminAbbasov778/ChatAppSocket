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
    private val TAG = "SocketHandler"

    private val _eventsFlow = MutableStateFlow<SocketEvent>(SocketEvent.UserOnline(""))
    val eventsFlow: StateFlow<SocketEvent> = _eventsFlow

    private val _messagesFlow = MutableSharedFlow<GetMessage>(replay = 0, extraBufferCapacity = 10)
    val messagesFlow: Flow<GetMessage> = _messagesFlow.asSharedFlow()

    fun connectSocket(currentUserId: String) {
        socket.connect()
        socket.off("receive_message")
        Log.d(TAG, "🔌 connectSocket() called with userId=$currentUserId")

        socket.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "✅ Socket connected, registering user: $currentUserId")
            socket.emit("register_user", currentUserId)
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e(TAG, "❌ Connect error: ${args.joinToString()}")
        }

        socket.on(Socket.EVENT_DISCONNECT) { args ->
            Log.d(TAG, "⚠️ Socket disconnected: ${args.joinToString()}")
        }

        // User list events
        socket.on("user_list") { args ->
            Log.d(TAG, "📥 user_list event: ${args.joinToString()}")
            val jsonString = args[0].toString()
            val users = try {
                gson.fromJson(jsonString, Array<UserData>::class.java).toList()
            } catch (e: Exception) {
                Log.e(TAG, "❌ Parsing user_list failed", e)
                emptyList()
            }
            Log.d(TAG, "✅ Parsed user_list: $users")
            _eventsFlow.tryEmit(SocketEvent.UserList(users))
        }

        socket.on("user_online") { args ->
            Log.d(TAG, "online: ")
            try {
                val jsonString = args[0].toString()           // args[0] JSON string olaraq gəlir
                val jsonMap = gson.fromJson(jsonString, Map::class.java)
                val userId = jsonMap["userId"] as? String
                if (userId != null) CoroutineScope(Dispatchers.IO).launch {
                    _eventsFlow.emit(SocketEvent.UserOnline(userId))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to parse user_online", e)
            }
        }

        socket.on("user_offline") { args ->
            Log.d(TAG, "offline: ")

            try {
                val jsonString = args[0].toString()
                val jsonMap = gson.fromJson(jsonString, Map::class.java)
                val userId = jsonMap["userId"] as? String
                if (userId != null) CoroutineScope(Dispatchers.IO).launch {
                    _eventsFlow.emit(SocketEvent.UserOffline(userId))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to parse user_offline", e)
            }
        }



        // Receive message event
        socket.on("receive_message") { args ->
            Log.d(TAG, "📥 receive_message event: ${args.joinToString()}")

            try {
                // args[0] artıq JSON string kimi gəlir
                val json = args[0].toString()
                val msg = gson.fromJson(json, GetMessage::class.java)
                Log.d(TAG, "✅ Parsed receive_message: $msg")
                _messagesFlow.tryEmit(msg)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to parse receive_message", e)
            }
        }



        Log.d(TAG, "🚀 socket.connect() called")
    }

    fun enterChat(userId: String) {

        socket.emit("enter_chat", userId)
    }


    fun sendMessage(sendMessage: SendMessage) {

        Log.d("SocketHandlerCheck", "emitSendMessage: ")

        val messageData = JSONObject()

        messageData.put("senderId", sendMessage.senderId)
        messageData.put("receiverId", sendMessage.receiverId)
        messageData.put("message", sendMessage.message)


        socket.emit("send_message", messageData)

        Log.d("SocketHandler", "emitSendMessage: $messageData")

    }
}
