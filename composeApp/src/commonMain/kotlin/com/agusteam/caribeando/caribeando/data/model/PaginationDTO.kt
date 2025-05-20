package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginationDTO<T>(
    val body: List<T>,
    val last: Boolean,
    val currentPage: Int,
    val totalPages: Int,
)
