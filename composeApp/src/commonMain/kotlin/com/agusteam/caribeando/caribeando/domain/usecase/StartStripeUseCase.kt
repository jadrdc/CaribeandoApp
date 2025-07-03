package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.caribeando.core.base.StripeParam
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse

expect class StartStripeUseCase(context: PlatformContext) {
    fun startStripe(stripe: StripePaymentIntentResponse)
    fun presentPaymentSheet(config: StripeConfiguration)
    fun setStripeParam(param: StripeParam)
}

expect class PlatformContext
expect class StripeConfiguration
