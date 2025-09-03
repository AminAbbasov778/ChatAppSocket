package com.example.chatapp.core.domain.usecases

import com.example.chatapp.core.domain.interfaces.UserRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private  val userRepository: UserRepository) {
    operator fun invoke() = userRepository.observeMessages()
}