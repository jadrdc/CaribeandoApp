package com.agusteam.caribeando.caribeando.core

import com.agusteam.caribeando.caribeando.core.base.StripeParam
import com.agusteam.caribeando.caribeando.core.base.StripePaymentResult

interface StripeNativeBridge : StripeParam {
    fun configure(
        publishableKey: String,
        customerId: String,
        ephemeralKey: String,
        paymentIntent: String
    )

    fun presentSheet()
    var onResult: ((StripePaymentResult) -> Unit)?

}
