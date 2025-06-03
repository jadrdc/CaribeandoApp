package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.LoginRepository
import com.agusteam.caribeando.domain.models.TokenMode

class GoogleSignInUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(token: String): OperationResult<TokenMode> {
        return repository.google(token)
    }
}


