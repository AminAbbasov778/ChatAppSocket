package com.example.chatapp.chatscreen.domain

import com.example.chatapp.core.domain.interfaces.UserRepository
import javax.inject.Inject

class EnterChatUseCase @Inject constructor(private val userRepository: UserRepository) {
   operator fun invoke(userId: String) = userRepository.enterChat(userId)
}