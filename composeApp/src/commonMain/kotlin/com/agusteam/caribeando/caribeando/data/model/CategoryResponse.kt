package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val id: String,
    val description: String,
    val image: String? = null,
    val active: Boolean
)
