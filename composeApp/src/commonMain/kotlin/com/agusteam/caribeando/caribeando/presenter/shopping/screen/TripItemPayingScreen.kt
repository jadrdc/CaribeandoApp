package com.agusteam.caribeando.presenter.shopping.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.domain.models.OrderDetailModel
import com.agusteam.caribeando.domain.models.PaymentModel
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.shopping.composable.ProviderItemOrderDetail
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingPaymentWay
import com.agusteam.caribeando.presenter.shopping.composable.TripItemPayDetail
import com.agusteam.caribeando.presenter.shopping.composable.TripItemPayHeader
import com.agusteam.caribeando.presenter.shopping.model.ShoppingDetailModel
import com.agusteam.caribeando.presenter.shopping.navigation.ShoppingItemPayingScreen
import com.agusteam.caribeando.presenter.stripe.PaymentEvents
import com.agusteam.caribeando.presenter.stripe.PaymentViewModel
import com.agusteam.caribeando.presenter.stripe.StripeButton
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_address
import caribeando.composeapp.generated.resources.ic_cash
import caribeando.composeapp.generated.resources.initial_payment
import caribeando.composeapp.generated.resources.leaving_date
import caribeando.composeapp.generated.resources.processing_payment
import caribeando.composeapp.generated.resources.starting_place
import caribeando.composeapp.generated.resources.total_payment

/**
 * 2026-08-20 (design handoff pass): spec screen 14/15 -- the pay button (`StripeButton`, already
 * a spec-matching `ActionButton` internally) is now pinned to a sticky bottom bar instead of
 * scrolling away as the last LazyColumn item, and the "Procesando pago" full-screen loading
 * overlay is restyled from a bare `CircularProgressIndicator` to the spec's translucent-page
 * overlay with a red-on-track ring and "Procesando pago..." label.
 */
@Composable
fun TripItemPayingScreen(
    onBackPressed: () -> Unit,
    onPaymentSuccessFull: (OrderDetailModel) -> Unit,
    onPaymentFailed: (reason: String, amount: Double) -> Unit = { _, _ -> },
    viewModel: PaymentViewModel = koinViewModel(),
    model: ShoppingItemPayingScreen
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val colors = caribeandoColors

    // 2026-08-20 (design handoff pass): screen 17, "Pago fallido" -- a real Stripe failure now
    // navigates to a dedicated recovery screen instead of just popping the generic ErrorModal.
    ObserveAsEvents(viewModel.events) { event ->
        if (event is PaymentEvents.OnPaymentFailed) {
            val attemptedAmount = if (state.value.selectedPaymentType == PaymentModel.TOTAL_PAYMENT)
                state.value.totalPayment else state.value.initialPayment
            onPaymentFailed(event.reason, attemptedAmount)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(
            PaymentEvents.InitialLoad(
                businessName=model.businessName,
                tripId = model.tripId,
                title = model.title,
                destiny = model.destiny,
                leavingTime = model.leavingTime,
                meetingPoint = model.meetingPoint,
                initialPayment = model.initialPayment.toDouble(),
                totalPayment = model.totalPayment.toDouble(),
                tripDetailId = model.tripDetailId,
            )
        )
    }
    val details = listOf(
        ShoppingDetailModel(
            title = stringResource(Res.string.initial_payment),
            description = formatMoney(state.value.initialPayment.toInt()),
            icon = Res.drawable.ic_cash
        ),
        ShoppingDetailModel(
            title = stringResource(Res.string.total_payment),
            description = formatMoney(state.value.totalPayment.toInt()),
            icon = Res.drawable.ic_cash
        ),
        ShoppingDetailModel(
            title = stringResource(Res.string.starting_place),
            description = state.value.meetingPoint,
            icon = Res.drawable.ic_address
        ),
        ShoppingDetailModel(
            title = stringResource(Res.string.leaving_date),
            description = state.value.leavingTime,
            icon = Res.drawable.ic_address
        ),
    )

    ErrorModal(title = state.value.errorModel?.title ?: "",
        message = state.value.errorModel?.message ?: "",
        showError = state.value.errorModel != null, onDismiss = {
            viewModel.handleEvent(PaymentEvents.OnErrorModalAccepted)
        })

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.padding(horizontal = 20.dp, vertical = 0.dp).padding(bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TripItemPayHeader(state.value) { onBackPressed() }
            }
            item {
                TripItemPayDetail(details)
            }
            item {
                ProviderItemOrderDetail(
                    businessMonth = model.businessMonth,
                    businessPhoto = state.value.businessPhoto,
                    businessName = model.businessName
                )
            }
            item {
                ShoppingPaymentWay(state = state.value) { event ->
                    viewModel.handleEvent(event)
                }
            }
        }

        Column(Modifier.fillMaxWidth().align(Alignment.BottomCenter).background(colors.stickyBar)) {
            HorizontalDivider(color = colors.hairline)
            // 2026-10-09 (design handoff): bottom bar = 20dp gutter, 14dp top / 18dp bottom.
            Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 18.dp)) {
                StripeButton(viewModel) {
                    onPaymentSuccessFull(
                        OrderDetailModel(
                            tripTitle = state.value.title,
                            amount = if (state.value.selectedPaymentType == PaymentModel.TOTAL_PAYMENT) state.value.totalPayment else state.value.initialPayment,
                            transactionId = state.value.stripeState?.paymentIntentId ?: "",
                            dateFrom = state.value.leavingTime,
                            dateTo = state.value.leavingTime,
                            galleryPhotos = state.value.galleryPhoto[0],
                            businessName = model.businessName,
                            businessPhoto = state.value.businessPhoto,
                            businessMonth = model.businessMonth,
                        )
                    )
                }
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
