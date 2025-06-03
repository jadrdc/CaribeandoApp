package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AppleSignUpRequest(
    val identityToken: String,
    val firstName: String?, // Only sent on first login
    val lastName: String?
)