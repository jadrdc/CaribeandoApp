package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestPasswordChangeModel(val email: String)