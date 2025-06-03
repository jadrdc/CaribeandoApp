package com.agusteam.caribeando.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UpcomingOrders(
    val hasBeenEvaluated: Boolean = true,
    val transactionId: String,
    val tripName: String,
    val tripDestiny: String,
    val tripImage: String,
    val providerName: String,
    val providerImage: String,
    val date: String,
    val providerMonth: Int = 0,
    val monthMissingForTrip: Int = 0,
    val timeUntilTrip: String = "",
    val tripImages: List<String> = listOf(),
    val dateFrom: String,
    val dateTo: String,
    val totalPayment: Double,
    val scheduledId: String,
)
