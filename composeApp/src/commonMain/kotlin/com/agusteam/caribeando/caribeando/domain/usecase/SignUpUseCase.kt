package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.LoginRepository
import com.agusteam.caribeando.domain.models.TokenMode

class SignUpUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(
        name: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        birthdate: String
    ): OperationResult<TokenMode> {
        return repository.signUpUser(name, lastName, phone, email, password, birthdate)
    }
}