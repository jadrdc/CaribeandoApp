package com.agusteam.caribeando.domain.models

data class TripDetailsModel(
    val description: String = "",
    val includedServices: List<String> = listOf(),
    val galleryPhotos: List<String> = listOf()
)
