package com.agusteam.caribeando.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UpcomingOrders(
    val hasBeenEvaluated: Boolean = true,
    val transactionId: String,
    val orderId: String = "",
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
    // 2026-08-20 (design handoff pass): new -- how much of [totalPayment] has actually been
    // paid, from the backend's new paid_amount field (backend gap #1). Defaults to 0.0 so any
    // caller not yet passing it (there shouldn't be any) fails safe rather than crashing.
    val paidAmount: Double = 0.0,
)
