package com.agusteam.caribeando.data.model

import com.agusteam.caribeando.caribeando.data.util.InstantIso8601Serializer
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TripDetails(
    val id: String = "",
    @Serializable(with = InstantIso8601Serializer::class)
    val leaving_time: Instant = Clock.System.now(),
    @Serializable(with = InstantIso8601Serializer::class)
    val returning_time: Instant = Clock.System.now(),
    val meeting_point: String = "",
    val initial_payment: Double = 0.0,
    val total_payment: Double = 0.0,
    val is_active: Boolean = false
)
