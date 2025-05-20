package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessModel(
    val address: String,
    val created_at: String,
    val description: String,
    val email: String,
    val id: String,
    val image: String,
    val name: String,
    val phone: String,
    val rnc: String
)