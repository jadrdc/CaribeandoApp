package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.UserDto
import com.agusteam.caribeando.domain.interfaces.UserProfileRepository

class GetUserProfileUseCase (private val repository: UserProfileRepository) {
    suspend operator fun invoke(): OperationResult<UserDto> {
        return repository.getProfileInfo()
    }
}