package com.agusteam.caribeando.data.model

import kotlinx.serialization.Serializable

/**
 * 2026-10-09: response of the backend's GET /support/contact (SupportController.kt) -- the real
 * support channels for the "Contáctanos" screen. A blank field means that channel isn't offered.
 */
@Serializable
data class ContactInfoResponse(
    val phone: String = "",
    val whatsapp: String = "",
    val email: String = "",
    val hours: String = "",
)
