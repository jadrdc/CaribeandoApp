package com.agusteam.caribeando.domain.usecase

import android.content.Context
import com.agusteam.caribeando.caribeando.core.base.StripeParam
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet

actual class StartStripeUseCase actual constructor(private val context: PlatformContext) {
    private var customerConfig: PaymentSheet.CustomerConfiguration? = null
    private var paymentIntentClientSecret: String = ""
    actual fun startStripe(stripe: StripePaymentIntentResponse) {
        customerConfig = PaymentSheet.CustomerConfiguration(
            id = stripe.customer,
            ephemeralKeySecret = stripe.ephemeralKey
        )
        paymentIntentClientSecret = stripe.paymentIntent
        PaymentConfiguration.init(context.context, stripe.publishableKey)
    }

    actual fun presentPaymentSheet(config: StripeConfiguration) {
        if (customerConfig != null && paymentIntentClientSecret.isNotBlank())
            config.paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                PaymentSheet.Configuration(
                    merchantDisplayName = "My merchant name",
                    customer = customerConfig,
                    allowsDelayedPaymentMethods = true,
                )
            )
    }


    actual fun setStripeParam(param: StripeParam) {

    }
}

actual class PlatformContext(val context: Context)
actual class StripeConfiguration(val paymentSheet: PaymentSheet)