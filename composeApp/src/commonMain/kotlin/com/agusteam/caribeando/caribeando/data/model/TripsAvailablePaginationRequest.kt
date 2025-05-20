package com.agusteam.caribeando.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TripsAvailablePaginationRequest(
    val category: String,
    val amount: Int = 0,
    val search: String = "",
    val page: Int = 0,
    val leavingTimeStart: Instant,
    val returningTimeEnd: Instant
)
