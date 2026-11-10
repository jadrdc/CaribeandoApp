package com.agusteam.caribeando.presenter.stripe

import androidx.compose.runtime.Composable

@Composable
// 2026-08-20 (design handoff pass): [label] lets a caller override the button's default
// "Procesar pago" text -- screen 20 ("Pagar faltante") wants the amount baked into the label
// ("Pagar RD$1,700"). Both actuals fall back to the existing default when null, so every
// existing call site (which doesn't pass it) is unaffected.
expect fun StripeButton(
    viewModel: PaymentViewModel,
    label: String? = null,
    onPaymentSuccessFull: () -> Unit
)
