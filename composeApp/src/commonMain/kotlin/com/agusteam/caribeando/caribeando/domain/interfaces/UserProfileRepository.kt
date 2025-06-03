package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.UserDto

interface UserProfileRepository {
    suspend fun getProfileInfo(): OperationResult<UserDto>
}
