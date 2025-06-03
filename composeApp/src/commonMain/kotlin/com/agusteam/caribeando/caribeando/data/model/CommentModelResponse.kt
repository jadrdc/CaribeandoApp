package com.agusteam.caribeando.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CommentModelResponse(
    val rating: Int = 0,
    val comment: String = "",
    val name: String = "",
    val image: String = ""
)