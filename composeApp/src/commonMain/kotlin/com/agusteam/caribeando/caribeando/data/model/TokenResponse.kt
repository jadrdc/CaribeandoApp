package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val phoneConfigured: Boolean = false,
    val birthdateConfigured: Boolean = false,
)