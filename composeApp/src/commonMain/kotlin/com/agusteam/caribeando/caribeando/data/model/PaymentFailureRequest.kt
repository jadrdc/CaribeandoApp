package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentFailureRequest(val reason: String, val orderId: String)
