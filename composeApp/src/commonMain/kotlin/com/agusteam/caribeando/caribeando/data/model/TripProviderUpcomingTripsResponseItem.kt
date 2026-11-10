package com.agusteam.caribeando.data.model

import com.agusteam.caribeando.caribeando.data.util.InstantIso8601Serializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TripProviderUpcomingTripsResponseItem(
    @Serializable(with = InstantIso8601Serializer::class)
    val leaving_time: Instant,
    val returning_time: String,
    val total_payment: Double = 0.0,
    val tripModel: TripModelSummary
)
