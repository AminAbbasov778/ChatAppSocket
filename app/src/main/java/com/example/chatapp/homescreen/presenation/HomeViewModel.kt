package com.example.chatapp.homescreen.presenation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Constants.MY_USER_ID
import com.example.chatapp.core.presentation.mappers.toDomain
import com.example.chatapp.core.presentation.mappers.toUi
import com.example.chatapp.homescreen.domain.ConnectUseCase
import com.example.chatapp.homescreen.domain.FilterUsersByQueryUseCase
import com.example.chatapp.core.domain.usecases.GetMessagesUseCase
import com.example.chatapp.core.domain.usecases.GetUsersUseCase
import com.example.chatapp.homescreen.domain.ObserveLastMessagesUseCase
import com.example.chatapp.core.presentation.utils.toReadableTime
import com.example.chatapp.core.presentation.utils.toTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject




@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val connectUseCase: ConnectUseCase,
    private val observeLastMessagesUseCase: ObserveLastMessagesUseCase,
    private val filterUsersByQueryUseCase: FilterUsersByQueryUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        getUsers()
        getLastMessagesInfo()
        getMessages()

    }
    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.OnSearchTextChanged -> {
                _state.update { current ->
                    val filtered =filterUsersByQueryUseCase(event.text.trim(),current.users.map { it.toDomain() })

                    current.copy(searchText = event.text, filteredUsers = filtered.map { it.toUi() })
                }
            }

            is HomeEvent.MarkMessagesAsRead -> markMessagesAsRead(event.id)
        }
    }
    fun markMessagesAsRead(userId: String) {
        _state.update { currentState ->
            val updatedUsers = currentState.users.map { user ->
                if (user.id == userId) user.copy(messageCount = 0) else user
            }
            currentState.copy(users = updatedUsers)
        }
    }


    private fun getUsers() {
        viewModelScope.launch {
            connectUseCase(MY_USER_ID)
            getUsersUseCase(MY_USER_ID).collect { user ->
                Log.d("HomeViewModel", "User received: $user")
                val currentList = _state.value.users.toMutableList()
                val index = currentList.indexOfFirst { it.id == user.id }
                if (index >= 0) {
                    currentList[index] = user.toUi()
                } else {
                    currentList.add(user.toUi())
                }
                _state.value = _state.value.copy(myData = currentList.filter { it.id == MY_USER_ID }.takeIf { it.isNotEmpty() }?.first() )
                _state.value = _state.value.copy(users = currentList)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMessages() {
        viewModelScope.launch {
            getMessagesUseCase().collect { msg ->
                _state.update { currentState ->
                    val updatedUsers = currentState.users.map { user ->
                        if (user.id == msg.senderId && msg.senderId != MY_USER_ID || user.id == msg.receiverId && msg.receiverId != MY_USER_ID) {
                            user.copy(
                                lastMessage = msg.message,
                                lastTime = msg.timestamp?.toReadableTime() ?: "",
                                messageCount = user.messageCount + 1
                            )
                        } else user
                    }
                    currentState.copy(users = updatedUsers)
                }
            }
        }
    }


    private fun getLastMessagesInfo() {
        viewModelScope.launch {
            observeLastMessagesUseCase().collect { lastMessages ->
                val updatedUsers = _state.value.users.map { user ->
                    val lastMsgInfo = lastMessages.find { it.userId == user.id && it.userId != MY_USER_ID }
                    val timestamp = lastMsgInfo?.lastTime?.takeIf { it.isNotBlank() }?.toLongOrNull()
                    user.copy(
                        lastMessage = lastMsgInfo?.lastMessage,
                        lastTime = timestamp?.toTimeString() ?: ""
                    )
                }
                _state.value = _state.value.copy(users = updatedUsers)
            }
        }
    }

}
