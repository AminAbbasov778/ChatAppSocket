package com.example.chatapp.core.domain.usecases

import com.example.chatapp.core.domain.interfaces.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor (private val repository: UserRepository) {
    operator fun invoke(currentUserId: String) = repository.observeUsers()
}