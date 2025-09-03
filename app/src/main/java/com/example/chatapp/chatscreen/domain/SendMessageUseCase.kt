package com.example.chatapp.chatscreen.domain

import com.example.chatapp.core.domain.interfaces.UserRepository
import com.example.chatapp.core.domain.models.MessageModel
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val userRepository: UserRepository)  {
    operator fun invoke(message: MessageModel) = userRepository.sendMessage(message)

}