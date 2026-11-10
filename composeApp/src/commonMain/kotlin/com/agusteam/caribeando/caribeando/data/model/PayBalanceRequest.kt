package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PayBalanceRequest(
    val transactionId: String
)
