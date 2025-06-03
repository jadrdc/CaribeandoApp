package com.agusteam.caribeando.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class TripProviderModel(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val description: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val month: Int = 0,
    val avatarUrl: String = "",
    val categoryModel: List<CategoryModel>,
    val offerTrips: Long,
    val activeTrips: Long
)
