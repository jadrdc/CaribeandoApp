package com.agusteam.caribeando.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingOrderTripScheduleModel(
    val is_active: Boolean = true,
    val leaving_time: Instant,
    val total_payment: Double,
    val returning_time: Instant,
    val tripModel: UpcomingOrderTripBusinessModel
)