package com.agusteam.caribeando.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TripProviderUpcomingTripsResponseItem(
    val leaving_time: Instant,
    val returning_time: String,
    val total_payment: Double = 0.0,
    val tripModel: TripModelSummary
)