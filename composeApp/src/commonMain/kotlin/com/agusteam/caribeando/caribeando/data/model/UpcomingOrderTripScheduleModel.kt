package com.agusteam.caribeando.data.model

import com.agusteam.caribeando.caribeando.data.util.InstantIso8601Serializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingOrderTripScheduleModel(
    val is_active: Boolean = true,
    @Serializable(with = InstantIso8601Serializer::class)
    val leaving_time: Instant,
    val total_payment: Double,
    // 2026-08-20 (design handoff pass): new field from the backend (paid_amount), defaulted so
    // an older backend response missing it still deserializes instead of failing the whole list.
    val paid_amount: Double = 0.0,
    @Serializable(with = InstantIso8601Serializer::class)
    val returning_time: Instant,
    val tripModel: UpcomingOrderTripBusinessModel
)
