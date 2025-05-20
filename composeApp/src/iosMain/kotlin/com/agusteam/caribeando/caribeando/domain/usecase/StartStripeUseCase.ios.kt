package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.data.model.StripePaymentIntentResponse

actual class StartStripeUseCase actual constructor(context: PlatformContext) {
    actual fun startStripe(stripe: StripePaymentIntentResponse) {
    }

    actual fun presentPaymentSheet() {
    }

    actual fun setConfig(config: StripeConfiguration) {
    }
}

actual class PlatformContext
actual class StripeConfiguration