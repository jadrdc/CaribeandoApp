package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.RefreshTokenRequest
import com.agusteam.caribeando.data.model.TokenResponse

interface TokenRepository {
    suspend fun refresh(model: RefreshTokenRequest): OperationResult<TokenResponse>
    suspend fun getToken(): String
    suspend fun getRefresh(): String
}