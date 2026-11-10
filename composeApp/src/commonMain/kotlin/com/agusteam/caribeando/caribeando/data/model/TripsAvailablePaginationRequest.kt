package com.agusteam.caribeando.data.model

import com.agusteam.caribeando.caribeando.data.util.InstantIso8601Serializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TripsAvailablePaginationRequest(
    val category: String,
    val amount: Int = 0,
    val search: String = "",
    val page: Int = 0,
    @Serializable(with = InstantIso8601Serializer::class)
    val leavingTimeStart: Instant,
    @Serializable(with = InstantIso8601Serializer::class)
    val returningTimeEnd: Instant
)
