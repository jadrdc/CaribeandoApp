package com.agusteam.caribeando.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val id:String,
    val description: String,
    var isSelected: Boolean = false,
    val image: String? = null,
)