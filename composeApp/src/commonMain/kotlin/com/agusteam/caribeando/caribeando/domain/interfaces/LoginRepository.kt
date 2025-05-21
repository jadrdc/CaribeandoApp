package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.TokenMode

interface LoginRepository {
    suspend fun login(email: String, password: String): OperationResult<TokenMode>
    suspend fun requestPasswordChangeEmail(email: String): OperationResult<String>
    suspend fun signUpUser(
        name: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): OperationResult<TokenMode>

    suspend fun google(token: String): OperationResult<TokenMode>
}