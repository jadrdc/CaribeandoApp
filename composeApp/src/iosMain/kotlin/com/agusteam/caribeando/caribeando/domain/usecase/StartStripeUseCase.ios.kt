package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.data.model.StripePaymentIntentResponse
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class StartStripeUseCase actual constructor(
    private val context: PlatformContext
) {
    // private val stripeBridge = StripeBridge.shared()

    actual fun startStripe(stripe: StripePaymentIntentResponse) {
        /*  stripeBridge.configure(
              publishableKey = stripe.publishableKey,
              customerId = stripe.customer,
              ephemeralKey = stripe.ephemeralKey,
              paymentIntent = stripe.paymentIntent
          )*/
    }

    actual fun presentPaymentSheet() {
        //     stripeBridge.presentSheet()
    }

    actual fun setConfig(config: StripeConfiguration) {
        // No-op
    }
}

actual class PlatformContext()
actual class StripeConfiguration()