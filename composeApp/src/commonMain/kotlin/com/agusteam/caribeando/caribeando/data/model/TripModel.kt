package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TripModel(
    val cancellation_policy: String,
    val description: String,
    val destiny: String,
    val id: String,
    val images: List<String>,
    val lat: Double,
    val lng: Double,
    val name: String,
    val businessModel: SummaryBusinessModel
)