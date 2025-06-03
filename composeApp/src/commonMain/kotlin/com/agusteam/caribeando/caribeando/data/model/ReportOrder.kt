package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportOrder(val order: String, val message: String)

