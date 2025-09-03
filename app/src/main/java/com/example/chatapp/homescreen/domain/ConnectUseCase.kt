package com.example.chatapp.homescreen.domain

import com.example.chatapp.core.domain.interfaces.UserRepository
import javax.inject.Inject

class ConnectUseCase @Inject constructor(val userRepository: UserRepository)  {
    operator fun invoke(currentUserId: String) {
        userRepository.connect(currentUserId)
    }

}