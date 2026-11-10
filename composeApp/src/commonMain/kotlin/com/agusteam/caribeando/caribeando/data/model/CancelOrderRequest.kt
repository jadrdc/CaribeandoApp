package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderRequest(
    val reason: String? = null
)
