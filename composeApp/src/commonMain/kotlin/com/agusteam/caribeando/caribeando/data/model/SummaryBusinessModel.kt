package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SummaryBusinessModel(val id: String, val name: String, val image: String, val month: Int)
