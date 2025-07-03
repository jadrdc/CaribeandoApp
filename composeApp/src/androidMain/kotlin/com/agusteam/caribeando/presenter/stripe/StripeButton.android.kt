package com.agusteam.caribeando.presenter.stripe

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.process_payment
import com.agusteam.caribeando.domain.usecase.StripeConfiguration
import com.agusteam.caribeando.presenter.common.ActionButton
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun StripeButton(viewModel: PaymentViewModel, onPaymentSuccessFull: () -> Unit) {
    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                viewModel.handleEvent(
                    PaymentEvents.CanceledPayment()
                )
            }

            is PaymentSheetResult.Failed -> {
                viewModel.handleEvent(
                    PaymentEvents.FailedPayment(
                        "Pago cancelado", reason = paymentSheetResult.error.localizedMessage ?: ""
                    )
                )
            }

            is PaymentSheetResult.Completed -> {
                viewModel.handleEvent(PaymentEvents.StripePaymentSucess)
                onPaymentSuccessFull()
            }
        }
    }

    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)

    ActionButton(
        text = stringResource(Res.string.process_payment),
        modifier = Modifier.fillMaxWidth()
    ) {
        viewModel.handleEvent(PaymentEvents.OnStripePaymentStart(StripeConfiguration(paymentSheet)))
    }
}

