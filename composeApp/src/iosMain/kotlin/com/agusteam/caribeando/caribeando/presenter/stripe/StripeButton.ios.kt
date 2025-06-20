package com.agusteam.caribeando.presenter.stripe

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.process_payment
import com.agusteam.caribeando.presenter.common.ActionButton
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun StripeButton(
    viewModel: PaymentViewModel,
    onPaymentSuccessFull: () -> Unit
) {
    ActionButton(
        text = stringResource(Res.string.process_payment),
        modifier = Modifier.fillMaxWidth()
    ) {
        viewModel.handleEvent(PaymentEvents.OnStripePaymentStart)
    }
}