package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.LoginRepository
import com.agusteam.caribeando.domain.models.TokenMode

class AppleSignUpUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(
        token: String,
        firstName: String,
        lastName: String
    ): OperationResult<TokenMode> {
        return repository.apple(identityToken = token, firstName = firstName, lastName = lastName)
    }
}