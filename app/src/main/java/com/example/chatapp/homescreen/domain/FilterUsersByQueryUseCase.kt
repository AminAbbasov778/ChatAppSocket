package com.example.chatapp.homescreen.domain

import com.example.chatapp.core.domain.models.UserModel
import javax.inject.Inject

class FilterUsersByQueryUseCase @Inject constructor() {
    operator fun invoke(query: String, users: List<UserModel>): List<UserModel> {
      return users.filter {
            it.username.contains(query, ignoreCase = true)
        }

    }
}