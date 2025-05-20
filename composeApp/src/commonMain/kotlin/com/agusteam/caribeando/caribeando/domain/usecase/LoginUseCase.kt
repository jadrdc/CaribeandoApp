package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.LoginRepository
import com.agusteam.caribeando.domain.models.TokenMode

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(username: String, password: String): OperationResult<TokenMode> {
        return repository.login(username, password)
    }
}