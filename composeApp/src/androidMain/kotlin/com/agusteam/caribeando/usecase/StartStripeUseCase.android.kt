package com.agusteam.caribeando.domain.usecase

import android.content.Context
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet

actual class StartStripeUseCase actual constructor(private val context: PlatformContext) {
    private var customerConfig: PaymentSheet.CustomerConfiguration? = null
    private var paymentIntentClientSecret: String = ""
    private var paymentSheet: PaymentSheet? = null
    actual fun startStripe(stripe: StripePaymentIntentResponse) {
        customerConfig = PaymentSheet.CustomerConfiguration(
            id = stripe.customer,
            ephemeralKeySecret = stripe.ephemeralKey
        )
        paymentIntentClientSecret = stripe.paymentIntent
        PaymentConfiguration.init(context.context, stripe.publishableKey)
    }

    actual fun presentPaymentSheet() {
        if (customerConfig != null && paymentIntentClientSecret.isNotBlank())
            paymentSheet?.presentWithPaymentIntent(
                paymentIntentClientSecret,
                PaymentSheet.Configuration(
                    merchantDisplayName = "My merchant name",
                    customer = customerConfig,
                    allowsDelayedPaymentMethods = true
                )
            )
    }

    actual fun setConfig(config: StripeConfiguration) {
        if (paymentSheet == null) {
            paymentSheet = config.paymentSheet
        }
    }
}

actual class PlatformContext(val context: Context)
actual class StripeConfiguration(val paymentSheet: PaymentSheet)