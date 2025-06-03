package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val version: Int,
    val lastname: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val birthdate: String,
    val avatarUrl: String? = null
)
