package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.caribeando.core.StripeNativeBridge
import com.agusteam.caribeando.caribeando.core.base.StripeParam
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse

actual class StartStripeUseCase actual constructor(
    private val context: PlatformContext
) {
    private var stripeBridge: StripeNativeBridge? = null

    actual fun startStripe(stripe: StripePaymentIntentResponse) {
        stripeBridge?.configure(
            publishableKey = stripe.publishableKey,
            customerId = stripe.customer,
            ephemeralKey = stripe.ephemeralKey,
            paymentIntent = stripe.paymentIntent
        )
    }

    actual fun presentPaymentSheet(config: StripeConfiguration) {
        stripeBridge?.presentSheet()
    }


    actual fun setStripeParam(param: StripeParam) {
        this.stripeBridge = param as StripeNativeBridge

    }
}

actual class PlatformContext()
actual class StripeConfiguration()

