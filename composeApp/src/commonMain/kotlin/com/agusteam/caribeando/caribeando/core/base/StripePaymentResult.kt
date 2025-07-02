package com.agusteam.caribeando.caribeando.core.base


sealed class StripePaymentResult {
    data object Completed : StripePaymentResult()
    data object Canceled : StripePaymentResult()
    data class Failed(val message: String) : StripePaymentResult()
}
