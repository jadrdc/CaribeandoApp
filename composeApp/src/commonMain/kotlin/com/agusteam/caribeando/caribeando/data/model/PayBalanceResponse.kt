package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PayBalanceResponse(
    val orderId: String,
    val paidAmount: Double,
    val totalPayment: Double,
    val fullyPaid: Boolean,
    val message: String
)
