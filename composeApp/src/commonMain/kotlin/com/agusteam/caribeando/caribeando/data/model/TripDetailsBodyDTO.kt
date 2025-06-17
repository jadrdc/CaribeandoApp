package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TripDetailsBodyDTO(
    val images: List<String>,
    val services: List<String>,
    val businessImage: String = "",
    val cancellationPolicy: String = "",
    val description: String
)