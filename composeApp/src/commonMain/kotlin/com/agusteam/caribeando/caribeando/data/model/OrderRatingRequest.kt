package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderRatingRequest(val rating: Double, val orderId: String, val comment: String)