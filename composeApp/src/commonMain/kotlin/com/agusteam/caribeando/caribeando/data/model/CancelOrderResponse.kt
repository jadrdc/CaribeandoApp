package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderResponse(
    val status: String,
    val refundEligible: Boolean,
    val message: String
)
