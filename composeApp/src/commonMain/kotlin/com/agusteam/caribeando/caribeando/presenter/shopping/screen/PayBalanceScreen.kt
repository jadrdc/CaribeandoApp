package com.agusteam.caribeando.presenter.shopping.screen

import caribeando.composeapp.generated.resources.cancellation_refund_window
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ListGroup
import com.agusteam.caribeando.presenter.common.ListGroupLabelValueRow
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.shopping.navigation.PayBalanceScreenRoute
import com.agusteam.caribeando.presenter.stripe.PaymentEvents
import com.agusteam.caribeando.presenter.stripe.PaymentViewModel
import com.agusteam.caribeando.presenter.stripe.StripeButton
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.pay_balance_failed_message
import caribeando.composeapp.generated.resources.payment_error_title
import caribeando.composeapp.generated.resources.balance_breakdown_header
import caribeando.composeapp.generated.resources.balance_covers_full_trip
import caribeando.composeapp.generated.resources.balance_remaining_label
import caribeando.composeapp.generated.resources.order_label
import caribeando.composeapp.generated.resources.pay_amount_button
import caribeando.composeapp.generated.resources.pay_balance_action
import caribeando.composeapp.generated.resources.payment_to_pay
import caribeando.composeapp.generated.resources.processing_payment
import caribeando.composeapp.generated.resources.reservation_paid_label
import caribeando.composeapp.generated.resources.total_payment

/**
 * The handoff's "20. Pagar faltante" -- the gap the README calls "the most important" ("Checkout
 * copy already promises 'paga luego el faltante'... but nothing lets a user pay the remainder").
 *
 * 2026-08-20 (design handoff pass): NEW screen. Reuses `PaymentViewModel`/`StripeButton` (the
 * same Stripe PaymentSheet plumbing screen 14 uses) via `PaymentEvents.InitialLoad(existingOrderId
 * = ...)` instead of duplicating platform-specific Stripe integration -- see the dated comment on
 * `PaymentViewModel`'s `existingOrderId` param for the branch logic. Backend: `POST
 * order/{id}/pay-balance` (new endpoint, `OrderController.kt`), which increments the real
 * `Order.amount` and is guarded server-side (order must be SUCCESS, balance must be > 0).
 *
 * Deviations from spec, flagged rather than faked:
 * - Title uses the shared `NavigationBar` (25/1.25/600) rather than the spec's one-off 600/17 --
 *   same precedent as every other pushed screen in this pass (see `ReportOrderIssueScreen.kt`),
 *   for one consistent header instead of a per-screen pixel size.
 * - The spec's standalone "Información de pago" row is omitted here: on screen 19 that row shows
 *   a payment-method value the app has no real data source for (no card-brand/method field is
 *   tracked client-side), and repeating "Orden <código>" here would just duplicate the trip
 *   summary card's third line above. Rather than fabricate a method value, this section is left
 *   out; the real breakdown (Pago total / Reserva pagada / Faltante) is the one the user actually
 *   needs to decide whether to pay.
 * - "the cancellation line" (spec's own, lighter wording vs. screen 19's "cancellation panel") is
 *   rendered as a single caption line, not the bordered `CancellationPolicy` panel.
 */
@Composable
fun PayBalanceScreen(
    model: PayBalanceScreenRoute,
    onBackPressed: () -> Unit,
    onPaymentSuccessFull: () -> Unit,
    viewModel: PaymentViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()

    // A failed/cancelled Stripe attempt no longer touches the real order (see the
    // FailedPayment/CanceledPayment guards added to PaymentViewModel the same day) -- it just
    // needs to surface an error here so the user knows to retry. Reuses the same
    // SetErrorMessage -> ErrorModal plumbing every other payment screen already has, rather than
    // building a second error UI.
    val paymentErrorTitle = stringResource(Res.string.payment_error_title)
    val paymentErrorFallback = stringResource(Res.string.pay_balance_failed_message)
    ObserveAsEvents(viewModel.events) { event ->
        if (event is PaymentEvents.OnPaymentFailed) {
            viewModel.handleEvent(
                PaymentEvents.SetErrorMessage(
                    title = paymentErrorTitle,
                    message = event.reason.ifBlank { paymentErrorFallback }
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(
            PaymentEvents.InitialLoad(
                businessName = model.businessName,
                title = model.tripTitle,
                totalPayment = model.totalPayment.toDouble(),
                existingOrderId = model.orderId,
                // 2026-10-09: charge only what's left, not the full trip price again.
                balanceDue = (model.totalPayment - model.paidAmount).coerceAtLeast(0).toDouble(),
            )
        )
    }

    val faltante = (model.totalPayment - model.paidAmount).coerceAtLeast(0)

    ErrorModal(
        title = state.value.errorModel?.title ?: "",
        message = state.value.errorModel?.message ?: "",
        showError = state.value.errorModel != null,
        onDismiss = { viewModel.handleEvent(PaymentEvents.OnErrorModalAccepted) }
    )

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.padding(bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            item {
                Box(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    NavigationBar(title = stringResource(Res.string.pay_balance_action)) { onBackPressed() }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .border(1.dp, colors.panelBorder, Radii.panelShape)
                        .padding(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AsyncImage(
                            modifier = Modifier.size(78.dp).clip(RoundedCornerShape(12.dp)),
                            model = model.businessPhoto,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                        Column {
                            Text(
                                text = model.tripTitle,
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (model.businessName.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.padding(top = 4.dp),
                                    text = model.businessName,
                                    color = colors.textSecondary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "${stringResource(Res.string.order_label)} ${model.transactionId}",
                        color = colors.textSecondary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .background(colors.redTint, RoundedCornerShape(18.dp))
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.payment_to_pay).uppercase(),
                        color = colors.textSecondary,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = formatMoney(faltante),
                        color = colors.redFill,
                        fontSize = 34.sp,
                        lineHeight = 37.4.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(Res.string.balance_covers_full_trip),
                        color = colors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            item {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = stringResource(Res.string.balance_breakdown_header),
                        color = colors.textPrimary,
                        style = textStyles.sectionHeader,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    ListGroup(rows = listOf(
                        { ListGroupLabelValueRow(stringResource(Res.string.total_payment), formatMoney(model.totalPayment)) },
                        { ListGroupLabelValueRow(stringResource(Res.string.reservation_paid_label), "− ${formatMoney(model.paidAmount)}") },
                        {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(colors.subtleFill)
                                    .padding(horizontal = 15.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(Res.string.balance_remaining_label),
                                    color = colors.textPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = formatMoney(faltante),
                                    color = colors.redFill,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    ))
                }
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(Res.string.cancellation_refund_window),
                    color = colors.textSecondary,
                    fontSize = 11.5.sp,
                    lineHeight = 18.4.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Column(Modifier.fillMaxWidth().align(Alignment.BottomCenter).background(colors.stickyBar)) {
            HorizontalDivider(color = colors.hairline)
            // 2026-10-09 (design handoff): bottom bar = 20dp gutter, 14dp top / 18dp bottom.
            Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 18.dp)) {
                StripeButton(
                    viewModel = viewModel,
                    label = stringResource(
                        Res.string.pay_amount_button,
                        formatMoney(faltante)
                    ),
                    onPaymentSuccessFull = onPaymentSuccessFull
                )
            }
        }

        if (state.value.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.pageBackground.copy(alpha = 0.86f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(44.dp),
                        color = colors.redFill,
                        trackColor = colors.spinnerTrack,
                        strokeWidth = 3.dp
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = stringResource(Res.string.processing_payment),
                        color = colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
