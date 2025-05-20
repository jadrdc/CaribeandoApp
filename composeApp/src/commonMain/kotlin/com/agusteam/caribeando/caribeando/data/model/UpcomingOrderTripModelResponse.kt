package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpcomingOrderTripModelResponse(
    val id: String,
    val evaluated: Boolean = false,
    val tripScheduleModel: UpcomingOrderTripScheduleModel
)