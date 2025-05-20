package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessProfileModel(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val rnc: String,
    val description: String,
    val image: String,
    val month: Int,
    val categories: List<CategoryResponse> = listOf(),
    val tripOffers: Int = 0
)
