package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpcomingTripBusinessProvider(
    val image: String,
    val name: String,
    val month: Int
)