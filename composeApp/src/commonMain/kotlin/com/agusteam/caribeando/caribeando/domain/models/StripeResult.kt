package com.agusteam.caribeando.domain.models

sealed interface StripeResult {
    data object StripeCanceled : StripeResult
    data object StripeSuccess : StripeResult
    data object Initial : StripeResult

    data class StripeError(val message: String) : StripeResult
}