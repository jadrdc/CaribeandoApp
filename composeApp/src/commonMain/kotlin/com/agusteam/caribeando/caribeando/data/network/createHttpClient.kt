package com.agusteam.caribeando.data.network


import com.agusteam.caribeando.data.model.Token
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
import kotlinx.serialization.json.Json

fun createHttpClient(
    engine: HttpClientEngine,
): HttpClient {
    return HttpClient(engine) {
        // Configuración de logging
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR CLIENT: $message")
                }
            }
        }

        // Configuración por defecto para todas las solicitudes
        defaultRequest {
            // Asegurarse de que el token se envía en cada solicitud
            if (Token.token.isNotBlank()) {
                header(HttpHeaders.Authorization, "Bearer ${Token.token}")
                println("➡️ ADDING AUTH HEADER: Bearer ${Token.token}")
            }
        }

        // Observador para loguear detalles de respuestas
        install(ResponseObserver) {
            onResponse { response ->
                println("⬅ RESPONSE: ${response.status.value} ${response.status.description}")
                println("⬅️ HEADERS:")
                response.headers.forEach { name, values ->
                    println("   $name: ${values.joinToString()}")
                }

                // Verificar si hay problemas de autenticación
                if (response.status.value == 401) {
                    println("❌ AUTHENTICATION ERROR: Token may be invalid or expired")
                    println("🔑 CURRENT TOKEN: ${Token.token}")
                }
            }
        }

        // Configuración de timeout
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }

        // Configuración de serialización
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }

        // Configuración de autenticación
        install(Auth) {
            bearer {
                // Cargar tokens
                loadTokens {
                    println("🔄 LOADING TOKENS - Access: ${Token.token.take(15)}...")
                    BearerTokens(
                        accessToken = Token.token,
                        refreshToken = Token.refreshToken
                    )
                }

                // Configuración para refrescar tokens
                refreshTokens {
                    println("🔄 ATTEMPTING TO REFRESH TOKEN")
                    // Aquí deberías implementar la lógica para refrescar el token
                    // Por ejemplo, llamar a tu API de autenticación

                    // Si el refresco es exitoso:
                    try {
                        // Ejemplo: val newTokens = authRepository.refreshToken(Token.refreshToken)
                        // Token.token = newTokens.accessToken
                        // Token.refreshToken = newTokens.refreshToken

                        println("✅ TOKEN REFRESHED SUCCESSFULLY")
                        BearerTokens(
                            accessToken = Token.token,
                            refreshToken = Token.refreshToken
                        )
                    } catch (e: Exception) {
                        println("❌ TOKEN REFRESH FAILED: ${e.message}")
                        null // Devolver null si el refresco falla
                    }
                }

                // Configuración para determinar cuándo refrescar
                sendWithoutRequest { request ->
                    // Solo enviar el token para ciertas rutas (opcional)
                    // Por ejemplo, no enviar para rutas de login/registro
                    !request.url.encodedPath.startsWith("/auth/login")
                }
            }
        }
    }
}