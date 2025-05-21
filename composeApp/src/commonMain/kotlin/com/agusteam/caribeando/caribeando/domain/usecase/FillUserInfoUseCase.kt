package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.LoginRepository
import com.agusteam.caribeando.domain.models.TokenMode

class FillUserInfoUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(phone: String, email: String): OperationResult<TokenMode> {
        return repository.fillUserInformation(phone = phone, email)
    }
}