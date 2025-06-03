package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StripePaymentIntentRequest(val amount: Double, val description: String)
