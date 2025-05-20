package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpcomingOrderTripBusinessModel(
    val businessModel: UpcomingTripBusinessProvider,
    val destiny: String,
    val images: List<String>,
    val name: String
)