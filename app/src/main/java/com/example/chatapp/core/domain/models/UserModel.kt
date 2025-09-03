package com.example.chatapp.core.domain.models

data class UserModel( val id: String,
                 val username: String,
                 val photo: String,
    val isOnline: Boolean ) {
}