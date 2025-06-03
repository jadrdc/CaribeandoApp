package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.UserDto
import com.agusteam.caribeando.data.network.services.UserProfileService
import com.agusteam.caribeando.domain.interfaces.UserProfileRepository

class UserProfileRepositoryImp(private val service: UserProfileService) : UserProfileRepository {
    override suspend fun getProfileInfo(): OperationResult<UserDto> {
        return try {
            when (val result = service.getProfileInfo()) {
                is OperationResult.Success -> {
                    val model = result.data
                    OperationResult.Success(model)
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }
}