package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StripePaymentIntentResponse(
    val paymentIntent: String,
    val paymentIntentId: String,
    val customer: String,
    val publishableKey: String,
    val ephemeralKey: String
)