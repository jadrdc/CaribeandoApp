package com.agusteam.caribeando.presenter.stripe

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.process_payment
import com.agusteam.caribeando.LocalStripe
import com.agusteam.caribeando.caribeando.core.base.StripePaymentResult
import com.agusteam.caribeando.presenter.common.ActionButton
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun StripeButton(
    viewModel: PaymentViewModel,
    onPaymentSuccessFull: () -> Unit
) {
    val stripeBridge = LocalStripe.current

    LaunchedEffect(Unit) {
        viewModel.handleEvent(PaymentEvents.ConfigureStripeIOS(stripeBridge))
    }

    LaunchedEffect(Unit) {
        stripeBridge.onResult = { result ->
            when (result) {
                is StripePaymentResult.Completed -> {
                    viewModel.handleEvent(PaymentEvents.StripePaymentSucess)
                    onPaymentSuccessFull()
                }

                is StripePaymentResult.Canceled -> {
                    viewModel.handleEvent(
                        PaymentEvents.FailedPayment(
                            "Pago cancelado",
                            "El usuario canceló el proceso de pago."
                        )
                    )
                }

                is StripePaymentResult.Failed -> {
                    viewModel.handleEvent(
                        PaymentEvents.FailedPayment(
                            "Error en el pago",
                            result.message
                        )
                    )
                }
            }
        }
    }

    ActionButton(
        text = stringResource(Res.string.process_payment),
        modifier = Modifier.fillMaxWidth()
    ) {
        viewModel.handleEvent(PaymentEvents.OnStripePaymentStart)
    }
}