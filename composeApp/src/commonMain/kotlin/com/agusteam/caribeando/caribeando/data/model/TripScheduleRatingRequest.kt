package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TripScheduleRatingRequest(val rating: Double, val tripScheduleId: String, val comment: String)