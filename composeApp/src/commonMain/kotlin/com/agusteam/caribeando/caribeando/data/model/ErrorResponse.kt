package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String,
    val error: String="",
)
