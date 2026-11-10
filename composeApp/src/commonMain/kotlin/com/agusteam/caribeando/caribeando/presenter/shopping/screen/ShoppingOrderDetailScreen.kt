package com.agusteam.caribeando.presenter.shopping.screen

import com.agusteam.caribeando.presenter.common.QrCodeImage
import caribeando.composeapp.generated.resources.ticket_qr_caption
import caribeando.composeapp.generated.resources.payment_confirmation_code
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.size
import caribeando.composeapp.generated.resources.cancel_reservation_question
import caribeando.composeapp.generated.resources.cancellation_refund_window
import caribeando.composeapp.generated.resources.cancellation_title
import caribeando.composeapp.generated.resources.cancelling
import caribeando.composeapp.generated.resources.order_status_completed
import caribeando.composeapp.generated.resources.order_status_upcoming
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.BackButton
import com.agusteam.caribeando.presenter.common.BottomModalSheet
import com.agusteam.caribeando.presenter.common.CancellationPolicy
import com.agusteam.caribeando.presenter.common.dashedBorder
import com.agusteam.caribeando.presenter.common.ListGroup
import com.agusteam.caribeando.presenter.common.ListGroupLabelValueRow
import com.agusteam.caribeando.presenter.common.PendingReview
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.shopping.composable.DateTripRowInformation
import com.agusteam.caribeando.presenter.shopping.composable.ProviderItemOrderDetail
import com.agusteam.caribeando.presenter.shopping.navigation.ShoppingOrderDetailScreenRoute
import com.agusteam.caribeando.presenter.shopping.viewmodels.ShoppingOrderDetailEvent
import com.agusteam.caribeando.presenter.shopping.viewmodels.ShoppingOrderDetailViewModel
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.agency
import caribeando.composeapp.generated.resources.amount_pay
import caribeando.composeapp.generated.resources.balance_due_description
import caribeando.composeapp.generated.resources.balance_due_label
import caribeando.composeapp.generated.resources.cancel_reservation_action
import caribeando.composeapp.generated.resources.pay_balance_action
import caribeando.composeapp.generated.resources.keep_reservation
import caribeando.composeapp.generated.resources.order_label
import caribeando.composeapp.generated.resources.ok
import caribeando.composeapp.generated.resources.payment_info
import caribeando.composeapp.generated.resources.pending
import caribeando.composeapp.generated.resources.ready
import caribeando.composeapp.generated.resources.report_order_issues
import caribeando.composeapp.generated.resources.reservation_details

/**
 * The handoff's "19. Detalles de la reserva". This is the REAL order-detail screen the handoff
 * describes -- `OrderItemDetailsScreen.kt` (same-sounding name) is actually a Wishlist trip-detail
 * view (`WishListOrderDetailViewModel`/`WishListItemDetailScreenRoute`), not this one; verified by
 * reading both files before touching either.
 *
 * 2026-08-20 (design handoff pass): restyled from a plain LazyColumn of ad-hoc section headers
 * into hero (210dp, back circle) + eyebrow/title/meta + a dashed confirmation-code block with a
 * status pill + "Información de pago" / trip-dates / provider / cancellation sections, per spec.
 *
 * 2026-08-20 (design handoff pass, part 2): backend gap #1 closed (`Order.amount` now tracks
 * real cumulative payment, `paidAmount` threaded through the route) -- the "Falta por pagar"
 * panel below is now real, not a placeholder, and links to the new screen 20 ("Pagar faltante").
 *
 * One thing the spec asks for that is still NOT built here, flagged rather than faked:
 * - "proveedor · N personas" / "Escríbele al proveedor": no passenger-count field exists, and the
 *   provider-contact action was already present in the code before this pass wrapped in
 *   `if (true == false)` -- i.e. a previous dev already intentionally disabled it because there's
 *   no real contact-provider flow. Left disabled rather than re-enabling a dead click.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingOrderDetailScreen(
    model: ShoppingOrderDetailScreenRoute,
    reportOrder: () -> Unit,
    onBackPressed: () -> Unit,
    hasItBeenEvaluted: Boolean = true,
    rateOrder: (orderId: String, tripTitle: String, tripImage: String, date: String, providerName: String) -> Unit,
    // 2026-08-20 (design handoff pass, part 2): screen 20 hookup -- defaults to a no-op so this
    // stays source-compatible with `ShoppingFlowNavigation.kt`'s other call site, which doesn't
    // (and, per its own dead-route comment, can't currently) wire this action.
    payBalance: () -> Unit = {},
    // 2026-08-20 (re-verification pass): screen 21's spec never describes a modal "result"
    // screen for a successful cancellation -- it just wants the traveler returned to the list
    // with a toast (the backend's own cancellation message), the same pattern already used for
    // "24. Calificar viaje"'s `showRatedToast`. Defaults to a no-op so this stays
    // source-compatible with any call site that doesn't wire it (there is currently only one,
    // `OrderHistoricNavigationFlow.kt`, which now does).
    onCancelSucceeded: (String) -> Unit = {},
    viewModel: ShoppingOrderDetailViewModel = koinViewModel()
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    // A successful cancellation navigates back immediately with a toast (see
    // [onCancelSucceeded] above) rather than showing a modal result screen the spec never asked
    // for. `DismissCancelResult` clears `cancelResultMessage` as this fires so the effect can't
    // re-trigger on the next recomposition before the pop actually happens.
    LaunchedEffect(state.cancelResultMessage, state.cancelSucceeded) {
        val message = state.cancelResultMessage
        if (message != null && state.cancelSucceeded) {
            viewModel.handleEvent(ShoppingOrderDetailEvent.DismissCancelResult)
            onCancelSucceeded(message)
        }
    }

    // A real failure (network/server error) still gets an inline result dialog -- the spec is
    // silent on error handling here, so this keeps the traveler on the same screen with a
    // message and a way to dismiss, rather than a bare silent failure.
    if (state.cancelResultMessage != null && !state.cancelSucceeded) {
        CancelResultDialog(
            message = state.cancelResultMessage,
            onDismiss = {
                viewModel.handleEvent(ShoppingOrderDetailEvent.DismissCancelResult)
            }
        )
    }

    // 2026-08-20 (screens 18-30 fidelity pass): the handoff's own "BottomSheet" component is
    // the shared chrome for confirmation prompts like this one -- created unconditionally here
    // (Material3's own recommendation for `rememberModalBottomSheetState`) and only wrapped in
    // `BottomModalSheet` when `showCancelConfirmation` is true, mirroring `ExploreScreen.kt`'s
    // filter-sheet pattern.
    val cancelSheetState = rememberModalBottomSheetState(true)
    if (state.showCancelConfirmation) {
        CancelConfirmationDialog(
            orderId = model.transactionId,
            sheetState = cancelSheetState,
            onConfirm = { viewModel.handleEvent(ShoppingOrderDetailEvent.ConfirmCancel(model.orderId)) },
            onDismiss = { viewModel.handleEvent(ShoppingOrderDetailEvent.DismissCancelConfirmation) }
        )
    }

    val statusLabel = when {
        model.isPending -> stringResource(Res.string.pending)
        model.isUpcoming -> stringResource(Res.string.order_status_upcoming)
        else -> stringResource(Res.string.order_status_completed)
    }

    Box(Modifier.fillMaxWidth()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Box {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth().height(210.dp).clip(Radii.cardShape),
                        model = model.galleryPhoto,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                    Box(Modifier.padding(16.dp)) { BackButton { onBackPressed() } }
                }
            }
            item {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = stringResource(Res.string.reservation_details).uppercase(),
                        color = colors.redFill,
                        style = textStyles.eyebrowCaps,
                    )
                    Text(
                        modifier = Modifier.padding(top = 6.dp),
                        text = model.tripTitle,
                        color = colors.textPrimary,
                        fontSize = 21.sp,
                        lineHeight = 26.25.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (model.dateFrom.isNotBlank() || model.dateTo.isNotBlank())
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = "${model.dateFrom} — ${model.dateTo}",
                            color = colors.textSecondary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Normal
                        )
                }
            }
            // 2026-08-20 (screens 18-30 fidelity pass): spec is "the confirmation block -- 1dp
            // dashed #CFD5DA, radius 16, 16dp padding -- with 'Código de confirmación' caps over
            // the code 600/19, and the status as a cream pill ... 600/11 #D62828." This block had
            // a solid subtleFill background instead of the dashed border (the app already has a
            // `dashedBorder` modifier for exactly this, first used on screen 16's "Listo" screen),
            // the wrong label text, a 15sp code instead of 19sp, and a navy status label instead
            // of red.
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .dashedBorder(colors.dashedBorder, cornerRadius = 16.dp)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.payment_confirmation_code).uppercase(),
                            color = colors.textSecondary,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = model.transactionId,
                            color = colors.textPrimary,
                            fontSize = 19.sp,
                            letterSpacing = 0.05.em,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(colors.cream, RoundedCornerShape(percent = 50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = statusLabel, color = colors.redFill, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            // Ticket: the confirmation code as a QR the operator can scan on the day of the trip.
            if (model.transactionId.isNotBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        QrCodeImage(
                            content = model.transactionId,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                        )
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = stringResource(Res.string.ticket_qr_caption),
                            color = colors.textSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            // 2026-08-20 (design handoff pass, part 2): screen 19's conditional "Falta por
            // pagar" panel -- now driven by the real paidAmount (backend gap #1), not faked.
            if (model.paidAmount < model.amount)
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .background(colors.redTint, Radii.panelShape)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(Res.string.balance_due_label),
                                color = colors.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = formatMoney(model.amount - model.paidAmount),
                                color = colors.redFill,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = stringResource(Res.string.balance_due_description),
                            color = colors.textSecondary,
                            fontSize = 11.5.sp,
                            lineHeight = 18.4.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Box(Modifier.padding(top = 14.dp)) {
                            ActionButton(
                                text = stringResource(Res.string.pay_balance_action),
                                modifier = Modifier.fillMaxWidth(),
                                onClick = payBalance
                            )
                        }
                    }
                }
            if (!hasItBeenEvaluted)
                item {
                    Box(Modifier.padding(horizontal = 20.dp)) {
                        PendingReview { rateOrder(model.transactionId, model.tripTitle, model.galleryPhoto, model.dateFrom, model.businessName) }
                    }
                }
            item {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = stringResource(Res.string.payment_info),
                        color = colors.textPrimary,
                        style = textStyles.sectionHeader,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    ListGroup(rows = listOf(
                        { ListGroupLabelValueRow(stringResource(Res.string.amount_pay), formatMoney(model.amount)) }
                    ))
                }
            }
            item {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    DateTripRowInformation(model.dateFrom, model.dateTo)
                }
            }
            item {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = stringResource(Res.string.agency),
                        color = colors.textPrimary,
                        style = textStyles.sectionHeader,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    ProviderItemOrderDetail(
                        businessMonth = model.businessMonth,
                        businessPhoto = model.businessPhoto,
                        businessName = model.businessName,
                        showDivider = false
                    )
                }
            }
            if (model.isUpcoming)
                item {
                    CancellationPolicy(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        title = stringResource(Res.string.cancellation_title),
                        description = stringResource(Res.string.cancellation_refund_window),
                    ) {}
                }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextLink(text = stringResource(Res.string.report_order_issues), onClick = reportOrder)
                    if (model.isUpcoming) {
                        Box(Modifier.padding(top = 14.dp)) {
                            TextLink(
                                text = if (state.isCancelling) stringResource(Res.string.cancelling) else stringResource(Res.string.cancel_reservation_action),
                                muted = true,
                                onClick = {
                                    if (!state.isCancelling) viewModel.handleEvent(ShoppingOrderDetailEvent.ShowCancelConfirmation)
                                }
                            )
                        }
                    }
                }
            }
            item {
                Box(Modifier.padding(horizontal = 20.dp).padding(top = 4.dp, bottom = 24.dp)) {
                    ActionButton(
                        text = stringResource(Res.string.ready),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}

/**
 * The handoff's "21. Cancelar reserva". Restyled from a raw Material `Dialog` + `Surface` +
 * default text styles into the shared component vocabulary (`ActionButton`, `TextLink`,
 * `ListGroupLabelValueRow`).
 *
 * The spec's ListGroup also wants a "Reembolso" row -- there's no computed refund amount
 * available before confirming (that's server-driven and only comes back in the *result* message
 * after `ConfirmCancel`, via `RequestOrderCancellationUseCase`), so only "Orden" is shown here;
 * the general refund-window policy stays as body copy above it rather than a fabricated number.
 *
 * 2026-08-20 (screens 18-30 fidelity pass): this was a plain `Dialog` + `Surface`, but the
 * handoff's own "BottomSheet" is the shared component for confirmation prompts everywhere else
 * in the app (e.g. screen 7's filter sheet) -- swapped for `BottomModalSheet`, following
 * `ExploreScreen.kt`'s established pattern of an externally-created `sheetState`. Also fixed
 * "Mantener reserva", which rendered muted/grey/underlined (500/12.5) instead of the spec's
 * "600/14 #003049, no fill" -- `TextLink` gained an optional `fontSize` override for this
 * (same null-default pattern as its existing `color`/`underline` overrides).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CancelConfirmationDialog(
    orderId: String,
    sheetState: SheetState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = caribeandoColors
    BottomModalSheet(onDismiss = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.cancel_reservation_question),
                color = colors.textPrimary,
                fontSize = 19.sp,
                lineHeight = 24.7.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(Res.string.cancellation_refund_window),
                color = colors.textBody,
                fontSize = 13.sp,
                lineHeight = 21.45.sp,
            )
            ListGroup(rows = listOf(
                { ListGroupLabelValueRow(stringResource(Res.string.order_label), orderId) }
            ))
            ActionButton(
                text = stringResource(Res.string.cancel_reservation_action),
                modifier = Modifier.fillMaxWidth()
            ) {
                onConfirm()
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextLink(
                    text = stringResource(Res.string.keep_reservation),
                    muted = false,
                    color = colors.textPrimary,
                    underline = false,
                    fontSize = 14.sp,
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun CancelResultDialog(
    message: String,
    onDismiss: () -> Unit
) {
    val colors = caribeandoColors
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = colors.pageBackground,
            modifier = Modifier.fillMaxWidth(),
            shape = Radii.cardShape,
        ) {
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = message, color = colors.textBody, fontSize = 13.sp, lineHeight = 21.45.sp)
                ActionButton(
                    text = stringResource(Res.string.ok),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onDismiss()
                }
            }
        }
    }
}
