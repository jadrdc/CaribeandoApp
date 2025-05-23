package com.agusteam.caribeando.data.network


import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.RefreshTokenRequest
import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.data.network.services.RefreshService
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RefreshTokenRequest(val refreshToken: String)

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val isPhoneConfigured: Boolean,
    val isBirthdateConfigured: Boolean
)

fun createHttpClient(
    engine: HttpClientEngine
): HttpClient {
    // Create a separate client for token refresh to avoid circular dependencies
    val refreshClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 30_000
            requestTimeoutMillis = 30_000
        }
    }

    return HttpClient(engine) {
        // Logging configuration
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR CLIENT: $message")
                }
            }
        }

        // Default configuration for all requests
        defaultRequest {
            // Make sure token is sent in each request
            if (Token.token.isNotBlank()) {
                header(HttpHeaders.Authorization, "Bearer ${Token.token}")
                println("➡️ ADDING AUTH HEADER: Bearer ${Token.token}")
            }
        }

        // Response observer for logging response details
        install(ResponseObserver) {
            onResponse { response ->
                println("⬅ RESPONSE: ${response.status.value} ${response.status.description}")
                println("⬅️ HEADERS:")
                response.headers.forEach { name, values ->
                    println("   $name: ${values.joinToString()}")
                }

                // Check for authentication problems
                if (response.status.value == 401) {
                    println("❌ AUTHENTICATION ERROR: Token may be invalid or expired")
                    println("🔑 CURRENT TOKEN: ${Token.token}")
                }
            }
        }

        // Timeout configuration
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }

        // Serialization configuration
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }

        // Authentication configuration
        install(Auth) {
            bearer {
                // Load tokens
                loadTokens {
                    println("🔄 LOADING TOKENS - Access: ${Token.token.take(15)}...")
                    BearerTokens(
                        accessToken = Token.token,
                        refreshToken = Token.refreshToken
                    )
                }

                // Token refresh configuration
                refreshTokens {

                    println("🔄 ATTEMPTING TO REFRESH TOKEN")

                    // Only attempt refresh if we have a refresh token
                    if (Token.refreshToken.isBlank()) {
                        println("❌ NO REFRESH TOKEN AVAILABLE")
                        return@refreshTokens null
                    }

                    try {
                        val service = RefreshService(refreshClient)
                        // Make the actual refresh token API call
                        val response = service.refresh(RefreshTokenRequest(Token.refreshToken))
                        when (response) {
                            is OperationResult.Error -> {
                                println("❌ TOKEN REFRESH FAILED: $response")
                                null
                            }

                            is OperationResult.Success -> {
                                Token.token = response.data.accessToken
                                Token.refreshToken = response.data.refreshToken

                                println("✅ TOKEN REFRESHED SUCCESSFULLY")
                                println("🔑 NEW ACCESS TOKEN: ${Token.token.take(15)}...")

                                // Return the new tokens
                                BearerTokens(
                                    accessToken = Token.token,
                                    refreshToken = Token.refreshToken
                                )
                            }
                        }

                    } catch (e: Exception) {
                        println("❌ TOKEN REFRESH FAILED: ${e.message}")
                        e.printStackTrace()
                        null // Return null if refresh fails
                    }
                }

                // Configuration to determine when to refresh
                sendWithoutRequest { request ->
                    // Only send token for certain routes (optional)
                    !request.url.encodedPath.startsWith("/auth/login") &&
                            !request.url.encodedPath.startsWith("/auth/signup") &&
                            !request.url.encodedPath.startsWith("/auth/refresh")
                }
            }
        }
    }
}