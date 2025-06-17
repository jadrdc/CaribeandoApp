package com.agusteam.caribeando.presenter.shopping.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.domain.models.OrderDetailModel
import com.agusteam.caribeando.domain.models.PaymentModel
import com.agusteam.caribeando.presenter.common.ErrorModal
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
import com.agusteam.caribeando.presenter.theme.primary
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_address
import caribeando.composeapp.generated.resources.ic_cash
import caribeando.composeapp.generated.resources.initial_payment
import caribeando.composeapp.generated.resources.leaving_date
import caribeando.composeapp.generated.resources.starting_place
import caribeando.composeapp.generated.resources.total_payment
import kotlinx.serialization.json.Json

@Composable
fun TripItemPayingScreen(
    onBackPressed: () -> Unit,
    onPaymentSuccessFull: (OrderDetailModel) -> Unit,
    viewModel: PaymentViewModel = koinViewModel(),
    model: ShoppingItemPayingScreen
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(
            PaymentEvents.InitialLoad(
                title = model.title,
                destiny = model.destiny,
                profilePhoto = model.profilePhoto,
                leavingTime = model.leavingTime,
                meetingPoint = model.meetingPoint,
                initialPayment = model.initialPayment.toDouble(),
                totalPayment = model.totalPayment.toDouble(),
                tripDetailId = model.tripDetailId,
                galleryPhoto = model.galleryPhoto
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
    if (state.value.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = false) {}, // Prevents interaction
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primary)
        }
    }
    LazyColumn(
        Modifier.padding(horizontal = 16.dp),
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
                businessPhoto = model.businessPhoto,
                businessName = model.businessName
            )
        }
        item {
            ShoppingPaymentWay(state = state.value) { event ->
                viewModel.handleEvent(event)
            }
        }
        item {
            Box(Modifier.padding(top = 16.dp)) {
                StripeButton(viewModel) {
                    onPaymentSuccessFull(
                        OrderDetailModel(
                            tripTitle = state.value.title,
                            amount = if (state.value.selectedPaymentType == PaymentModel.TOTAL_PAYMENT) state.value.totalPayment else state.value.initialPayment,
                            transactionId = state.value.stripeState?.paymentIntentId ?: "",
                            dateFrom = state.value.leavingTime,
                            dateTo = state.value.leavingTime,
                            galleryPhotos = state.value.galleryPhoto,
                            businessName = model.businessName,
                            businessPhoto = model.businessPhoto,
                            businessMonth = model.businessMonth,
                        )
                    )
                }
            }
        }
    }
}