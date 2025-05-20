package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.data.model.StripePaymentIntentResponse

expect class StartStripeUseCase(context: PlatformContext) {
    fun startStripe(stripe: StripePaymentIntentResponse)
    fun presentPaymentSheet()
    fun setConfig(config: StripeConfiguration)
}

expect class PlatformContext
expect class StripeConfiguration
