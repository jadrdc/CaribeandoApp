package com.agusteam.caribeando.data.network.services

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.GoogleTokenRequest
import com.agusteam.caribeando.data.model.LoginRequest
import com.agusteam.caribeando.data.model.TokenResponse
import com.agusteam.caribeando.data.model.RequestPasswordChangeModel
import com.agusteam.caribeando.data.model.UpdatePhoneAndBirthdateRequest
import com.agusteam.caribeando.data.model.UserSignUpRequest
import com.agusteam.caribeando.presenter.URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SignUpService(
    private val httpClient: HttpClient
) {

    suspend fun fillUserInformation(model: UpdatePhoneAndBirthdateRequest): OperationResult<TokenResponse> {
        return try {
            val response = httpClient.put(
                urlString = "${URL}auth/updatePhoneAndBirthdate"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(model)
            }
            return mapResponse<TokenResponse>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun googleSignIn(model: GoogleTokenRequest): OperationResult<TokenResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}auth/google"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(model)
            }
            if (response.status.value in 200..299) {
                httpClient.authProvider<BearerAuthProvider>()?.clearToken()
            }
            return mapResponse<TokenResponse>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun login(model: LoginRequest): OperationResult<TokenResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}auth/login"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(model)

            }
            if (response.status.value in 200..299) {
                httpClient.authProvider<BearerAuthProvider>()?.clearToken()
            }
            return mapResponse<TokenResponse>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun signUp(model: UserSignUpRequest): OperationResult<TokenResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}auth/signup"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(model)
            }
            if (response.status.value in 200..299) {
                httpClient.authProvider<BearerAuthProvider>()?.clearToken()
            }
            return mapResponse<TokenResponse>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun resetPasswordForEmail(model: RequestPasswordChangeModel): OperationResult<String> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}auth/resetPasswordForEmail"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(model)
            }
            return mapResponse<String>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

}

