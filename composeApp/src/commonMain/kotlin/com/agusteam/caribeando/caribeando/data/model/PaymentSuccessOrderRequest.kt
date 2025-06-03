package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentSuccessOrderRequest(val order: String, val transactionId: String)
