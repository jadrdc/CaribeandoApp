package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.LoginRepository

class RequestResetPasswordEmailUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(email:String): OperationResult<String> {
        return repository.requestPasswordChangeEmail(email)
    }
}