package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TripListPaginationResponseItem(
    val id: String,
    val name: String,
    val description: String,
    val destiny: String,
    val lat: Float,
    val lng: Float,
    val images: List<String>,
    val cancellation_policy: String,
    val isFavorite: Boolean,
    val businessModel: SummaryBusinessModel = SummaryBusinessModel("", "", "", 0),
    val details: TripDetails = TripDetails(id = ""),
    val reviewCount: Int = 0,
    val rating: Double = 0.0
)

