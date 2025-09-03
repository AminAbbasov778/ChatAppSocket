package com.example.chatapp.homescreen.presenation

sealed class HomeEvent() {
    data class OnSearchTextChanged(val text: String) : HomeEvent()
    data class MarkMessagesAsRead(val id: String) : HomeEvent()
}