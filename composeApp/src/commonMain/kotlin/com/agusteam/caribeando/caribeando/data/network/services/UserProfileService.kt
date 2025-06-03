package com.agusteam.caribeando.data.network.services

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.data.model.UserDto
import com.agusteam.caribeando.presenter.URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserProfileService(
    private val httpClient: HttpClient
) {

    suspend fun getProfileInfo(): OperationResult<UserDto> {
        println("CRUSEL TOKEN ${Token.token}")
        return try {
            val response = httpClient.get(
                urlString = "${URL}profile"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            return mapResponse<UserDto>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}