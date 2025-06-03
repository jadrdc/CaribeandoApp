package com.agusteam.caribeando.data.network.services

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.RefreshTokenRequest
import com.agusteam.caribeando.data.model.TokenResponse
import com.agusteam.caribeando.presenter.URL
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RefreshService(
    private val httpClient: HttpClient
) {
    suspend fun refresh(model: RefreshTokenRequest): OperationResult<TokenResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}auth/refresh"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(model)
            }
            return mapResponse<TokenResponse>(response)
        } catch (e: Exception) {
             mapExceptions(e)
        }
    }
}