package com.agusteam.caribeando.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderDetailModel(
    val tripTitle: String,
    val amount: Double,
    val transactionId: String,
    val dateFrom: String,
    val dateTo: String,
    val galleryPhotos: String,
    val businessName: String,
    val businessPhoto: String,
    val businessMonth: String
)