package com.agusteam.caribeando.presenter.stripe

import androidx.compose.runtime.Composable

@Composable
expect fun StripeButton(
    viewModel: PaymentViewModel,
    onPaymentSuccessFull: () -> Unit
)
