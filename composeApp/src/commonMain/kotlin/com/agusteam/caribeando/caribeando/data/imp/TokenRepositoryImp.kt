package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.RefreshTokenRequest
import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.data.model.TokenResponse
import com.agusteam.caribeando.data.network.services.RefreshService
import com.agusteam.caribeando.domain.interfaces.TokenRepository

class TokenRepositoryImp(private val service: RefreshService) : TokenRepository {
    override suspend fun refresh(model: RefreshTokenRequest): OperationResult<TokenResponse> {
        return when (val result = service.refresh(model)) {
            is OperationResult.Error -> {
                result
            }

            is OperationResult.Success -> {
                Token.token = result.data.accessToken
                Token.refreshToken = result.data.refreshToken
                result
            }
        }
    }


    override suspend fun getToken() = Token.token
    override suspend fun getRefresh() = Token.refreshToken
}