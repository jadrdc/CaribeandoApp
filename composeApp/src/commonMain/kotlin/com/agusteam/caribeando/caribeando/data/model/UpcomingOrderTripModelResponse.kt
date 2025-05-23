package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpcomingOrderTripModelResponse(
    val id: String,
    val scheduledId: String,
    val evaluated: Boolean = true,
    val tripScheduleModel: UpcomingOrderTripScheduleModel

)