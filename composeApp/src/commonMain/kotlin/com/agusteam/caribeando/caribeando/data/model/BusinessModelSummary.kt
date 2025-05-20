package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessModelSummary(val offerTrips: Long, val activeTrips: Long)
