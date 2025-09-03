package com.example.chatapp.homescreen.presenation

import com.example.chatapp.core.presentation.models.UserUi

data class HomeState(val users : List<UserUi> = emptyList(), val myData : UserUi? = null, val filteredUsers: List<UserUi> = emptyList(),
                     val searchText: String = "") {
}