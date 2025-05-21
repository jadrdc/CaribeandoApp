package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePhoneAndBirthdateRequest(
    val phone: String,
    val birthdate: String
)