package com.example.chatapp.homescreen.domain

import com.example.chatapp.core.domain.interfaces.UserRepository
import javax.inject.Inject

class ObserveLastMessagesUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.observeLastMessages()
}