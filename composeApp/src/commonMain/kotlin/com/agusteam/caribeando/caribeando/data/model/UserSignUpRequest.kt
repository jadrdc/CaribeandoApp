package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpRequest(
    val name: String,
    val password: String,
    val email: String,
    val phone: String,
    val lastname: String,
    val birthdate: String
)
