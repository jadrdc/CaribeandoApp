package com.agusteam.caribeando.data.network


import com.agusteam.caribeando.data.model.Token
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


fun createHttpClient(
    engine: HttpClientEngine,
    // tokenProvider: TokenRepository
): HttpClient {
    return HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = Token.token,//tokenProvider.getToken(),
                        refreshToken = Token.refreshToken
                    )
                }
                /*refreshTokens {
                    // This block is called when a 401 is received
                    val result =
                        tokenProvider.refresh(RefreshTokenRequest(tokenProvider.getRefresh()))
                    when (result) {
                        is OperationResult.Error -> null
                        is OperationResult.Success -> {
                            BearerTokens(
                                accessToken = result.data.accessToken,
                                refreshToken = result.data.refreshToken
                            )
                        }
                    }

                }*/
            }
        }
    }
}