package com.agusteam.caribeando.domain.models

data class TokenMode(
    val accessToken: String,
    val refreshToken: String,
    val isPhoneConfigured: Boolean = false,
    val isBirthdateConfigured: Boolean = false,
)
