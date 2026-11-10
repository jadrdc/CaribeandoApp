package com.agusteam.caribeando.presenter.stripe

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.core.base.StripeParam
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse
import com.agusteam.caribeando.data.util.LAST_NAME
import com.agusteam.caribeando.data.util.NAME
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.models.PaymentModel
import com.agusteam.caribeando.domain.usecase.CancelPaymentOrderUseCase
import com.agusteam.caribeando.domain.usecase.CreatePendingPaymentOrderUseCase
import com.agusteam.caribeando.domain.usecase.GetLocalProfileUseCase
import com.agusteam.caribeando.domain.usecase.GetStripePaymentIntentUseCase
import com.agusteam.caribeando.domain.usecase.GetTripDetailsUseCase
import com.agusteam.caribeando.domain.usecase.PayOrderBalanceUseCase
import com.agusteam.caribeando.domain.usecase.ProcessSuccessPaymentOrderUseCase
import com.agusteam.caribeando.domain.usecase.RemoveOrderUseCase
import com.agusteam.caribeando.domain.usecase.StartStripeUseCase
import com.agusteam.caribeando.domain.usecase.StripeConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModel(
    private val logger: CrashReporter,
    private val getStripePaymentIntentUseCase: GetStripePaymentIntentUseCase,
    private val startStripeUseCase: StartStripeUseCase,
    private val createPendingPaymentOrderUseCase: CreatePendingPaymentOrderUseCase,
    private val processSuccessPaymentOrderUseCase: ProcessSuccessPaymentOrderUseCase,
    private val cancelPaymentOrderUseCase: CancelPaymentOrderUseCase,
    val getLocalProfileUseCase: GetLocalProfileUseCase,
    val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val removeOrderUseCase: RemoveOrderUseCase,
    // 2026-08-20 (design handoff pass): screen 20, "Pagar faltante" -- reuses this ViewModel's
    // already-proven Stripe plumbing (getStripePaymentIntentUseCase/startStripeUseCase/
    // StripeButton) for an EXISTING order's balance instead of duplicating the whole
    // expect/actual Stripe integration. See PaymentState.existingOrderId below.
    private val payOrderBalanceUseCase: PayOrderBalanceUseCase
) : GenericViewModel<PaymentState, PaymentEvents>(
    PaymentState()
) {

    init {
        viewModelScope.launch {
            getLocalProfileUseCase().mapLatest { preference ->
                val nameKey = stringPreferencesKey(NAME)
                val lastNameKey = stringPreferencesKey(LAST_NAME)
                val name = "${preference[nameKey].orEmpty()} ${preference[lastNameKey].orEmpty()}"

                setState {
                    copy(fullName = name)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun handleEvent(events: PaymentEvents) {
        viewModelScope.launch {
            when (events) {
                is PaymentEvents.FailedPayment -> {
                    // 2026-08-20 (design handoff pass): was a generic ErrorModal for every
                    // payment failure. The handoff's "17. Pago fallido" is a dedicated full-
                    // screen recovery flow (retry / change method / back to explore), so this
                    // now fires a one-shot navigation event instead of the modal -- the backend
                    // call (cancelling the pending order so the traveler isn't charged) is
                    // unchanged.
                    // Guard added the same day: for a balance top-up (existingOrderId set),
                    // state.value.orderId is that SAME real, already-SUCCESS order (see
                    // OnStripePaymentStart below) -- cancelPaymentOrderUseCase exists to clean up
                    // a freshly-created PENDING order after a failed first payment, so calling it
                    // here would wrongly cancel/refund a real confirmed booking just because its
                    // balance top-up failed. Only fresh-checkout failures reach it.
                    if (state.value.existingOrderId.isBlank()) {
                        val result = cancelPaymentOrderUseCase(
                            state.value.orderId, events.reason
                        )
                        if (result is OperationResult.Error) {
                            logger.recordException(result.exception)
                        }
                    }
                    sendEvent(PaymentEvents.OnPaymentFailed(events.reason))
                }

                is PaymentEvents.OnPaymentFailed -> {
                    // No-op: this is a one-shot outgoing signal produced by the FailedPayment
                    // branch above (via sendEvent) for the UI to observe -- it is never actually
                    // dispatched back into handleEvent by any caller. This branch exists only to
                    // satisfy the compiler's exhaustiveness check on the sealed PaymentEvents
                    // interface.
                }

                is PaymentEvents.CanceledPayment -> {
                    // Same balance-top-up guard as FailedPayment above -- removeOrderUseCase
                    // deletes a freshly-created PENDING order the user backed out of; it must
                    // never run against an existing SUCCESS order being topped up.
                    if (state.value.existingOrderId.isBlank()) {
                        val result = removeOrderUseCase(
                            state.value.orderId
                        )
                        if (result is OperationResult.Error) {
                            logger.recordException(result.exception)
                        }
                    }
                }

                is PaymentEvents.ConfigureStripeIOS -> {
                    startStripeUseCase.setStripeParam(events.configuration)
                }

                is PaymentEvents.StripePaymentSucess -> {
                    val transactionId = state.value.stripeState?.paymentIntentId.orEmpty()
                    if (state.value.existingOrderId.isNotBlank()) {
                        // Balance top-up: the order is already SUCCESS, just record the extra
                        // payment -- calling processSuccessPaymentOrderUseCase here would be
                        // wrong, it's meant for a PENDING order's first (and only) charge.
                        val result = payOrderBalanceUseCase(state.value.existingOrderId, transactionId)
                        if (result is OperationResult.Error) {
                            logger.recordException(result.exception)
                        }
                    } else {
                        processSuccessPaymentOrderUseCase(state.value.orderId, transactionId)
                    }
                }

                is PaymentEvents.SetErrorMessage -> {
                    onErrorHappened(value = true, events.title, events.message)
                }

                is PaymentEvents.OnErrorModalAccepted -> {
                    onErrorHappened(false)
                }

                is PaymentEvents.InitialLoad -> {
                    setState {
                        copy(
                            businessName = events.businessName,
                            title = events.title,
                            destiny = events.destiny,
                            leavingTime = events.leavingTime,
                            meetingPoint = events.meetingPoint,
                            initialPayment = events.initialPayment,
                            totalPayment = events.totalPayment,
                            tripDetailId = events.tripDetailId,
                            existingOrderId = events.existingOrderId,
                            balanceDue = events.balanceDue,
                        )
                    }
                    // A balance top-up has nothing new to fetch -- the trip/order already exist
                    // and their display data was passed straight in via InitialLoad.
                    if (events.existingOrderId.isBlank()) {
                        getTripDetails(events.tripId)
                    }
                }

                is PaymentEvents.OnPaymentTypePicked -> {
                    changePaymentModel(events.paymentModel)
                }

                is PaymentEvents.OnStripePaymentStart -> {
                    updateState { copy(isLoading = true) }
                    val isBalanceTopUp = state.value.existingOrderId.isNotBlank()
                    val amount = when {
                        // 2026-10-09: was `totalPayment` -- for a balance top-up that charged the
                        // traveler the FULL trip price a second time instead of only what was
                        // left. Charge exactly the outstanding balance (the backend now also
                        // rejects any PaymentIntent whose amount doesn't match it).
                        isBalanceTopUp -> state.value.balanceDue
                        state.value.selectedPaymentType == PaymentModel.TOTAL_PAYMENT -> state.value.totalPayment
                        else -> state.value.initialPayment
                    }
                    val tripId = state.value.tripDetailId.takeIf { it.isNotBlank() }

                    val name = listOfNotNull(state.value.businessName.takeIf { it.isNotBlank() },
                        tripId,
                        state.value.title.takeIf { it.isNotBlank() },
                        state.value.leavingTime.takeIf { it.isNotBlank() }).joinToString(" ")

                    when (val result = getStripePaymentIntentUseCase(amount, name)) {
                        is OperationResult.Error -> {
                            logger.recordException(result.exception)
                            onErrorHappened(
                                true,
                                "Error en el proceso de pago",
                                message = result.exception.message
                                    ?: "No pudimos completar tu transacción en este momento. Por favor, verifica la información e inténtalo nuevamente"
                            )
                        }

                        is OperationResult.Success -> {
                            val stripe = result.data
                            setState { copy(stripeState = stripe) }
                            startStripeUseCase.startStripe(stripe)

                            if (isBalanceTopUp) {
                                // The order already exists (it's a SUCCESS order being topped
                                // up) -- no new pending order to create, unlike a fresh checkout.
                                setState { copy(orderId = state.value.existingOrderId) }
                                startStripeUseCase.presentPaymentSheet(events.configuration)
                            } else {
                                val tripId = state.value.tripDetailId.takeIf { it.isNotBlank() }
                                if (tripId != null) {
                                    when (val orderResult =
                                        createPendingPaymentOrderUseCase(tripId, amount)) {
                                        is OperationResult.Error -> {
                                            logger.recordException(orderResult.exception)
                                            onErrorHappened(
                                                true,
                                                "Error en el proceso de pago",
                                                message = orderResult.exception.message
                                                    ?: "No pudimos completar tu transacción en este momento. Por favor, verifica la información e inténtalo nuevamente"
                                            )
                                        }

                                        is OperationResult.Success -> {
                                            setState { copy(orderId = orderResult.data.orderId) }
                                            startStripeUseCase.presentPaymentSheet(events.configuration)
                                        }
                                    }
                                } else {
                                    onErrorHappened(
                                        true,
                                        "Viaje no disponible",
                                        "No se encontró el identificador del viaje."
                                    )
                                }
                            }
                        }
                    }
                    updateState { copy(isLoading = false) }
                }
            }
        }
    }

    private suspend fun changePaymentModel(paymentModel: PaymentModel) {
        setState {
            copy(selectedPaymentType = paymentModel)
        }
    }

    private suspend fun getTripDetails(tripId: String) {
        setState { copy(isLoading = true) }
        when (val result = getTripDetailsUseCase(tripId)) {
            is OperationResult.Success -> {
                val model = result.data
                updateState {
                    copy(
                        businessPhoto = model.businessImage,
                        galleryPhoto = model.images,
                        profilePhoto = model.images.firstOrNull() ?: ""
                    )
                }
            }

            is OperationResult.Error -> {
                logger.recordException(result.exception)
            }
        }
        setState { copy(isLoading = false) }
    }

    private suspend fun onErrorHappened(value: Boolean, title: String = "", message: String = "") {
        val errorModel = if (!value) null else ErrorModel(title = title, message = message)
        setState { copy(errorModel = errorModel) }
    }
}

data class PaymentState(
    val isLoading: Boolean = false,
    val fullName: String = "",
    val title: String = "",
    val destiny: String = "",
    val profilePhoto: String = "",
    val leavingTime: String = "",
    val meetingPoint: String = "",
    val initialPayment: Double = 0.0,
    val totalPayment: Double = 0.0,
    val stripeState: StripePaymentIntentResponse? = null,
    // 2026-10-09 (design handoff, screen 14): the default option is now the deposit
    // ("Pago de reserva solamente"), which the handoff lists first and pre-selects.
    val selectedPaymentType: PaymentModel = PaymentModel.BOOKING_PAYMENT,
    val errorModel: ErrorModel? = null,
    val tripDetailId: String = "",
    val orderId: String = "",
    val galleryPhoto: List<String> = emptyList(),
    val businessPhoto: String = "",
    val businessName: String = "",
    val itemProviderState: ItemProviderState = ItemProviderState(),
    // 2026-08-20 (design handoff pass): non-blank when this ViewModel is being used for
    // screen 20 "Pagar faltante" (paying the remaining balance on an already-SUCCESS order)
    // instead of a fresh checkout. See InitialLoad.existingOrderId below.
    val existingOrderId: String = "",
    // 2026-10-09: the outstanding amount to charge for a balance top-up (totalPayment - paid).
    val balanceDue: Double = 0.0
) : ViewModelState

sealed interface PaymentEvents {
    data class InitialLoad(
        val tripId: String = "",
        val title: String = "",
        val destiny: String = "",
        val profilePhoto: String = "",
        val leavingTime: String = "",
        val meetingPoint: String = "",
        val initialPayment: Double = 0.0,
        val totalPayment: Double = 0.0,
        val tripDetailId: String = "",
        val galleryPhoto: List<String> = emptyList(),
        val businessName: String,
        val existingOrderId: String = "",
        val balanceDue: Double = 0.0
    ) : PaymentEvents

    data class SetErrorMessage(val title: String, val message: String) : PaymentEvents
    data object OnErrorModalAccepted : PaymentEvents
    data class OnStripePaymentStart(val configuration: StripeConfiguration) : PaymentEvents
    data class OnPaymentTypePicked(val paymentModel: PaymentModel) : PaymentEvents
    data object StripePaymentSucess : PaymentEvents
    data class FailedPayment(val title: String, val reason: String) : PaymentEvents
    // One-shot outgoing signal (sent via `sendEvent`, never dispatched into `handleEvent` by a
    // caller) -- see the FailedPayment branch above.
    data class OnPaymentFailed(val reason: String) : PaymentEvents
    class CanceledPayment() : PaymentEvents
    data class ConfigureStripeIOS(val configuration: StripeParam) : PaymentEvents
}

data class ItemProviderState(
    val businessImage: String = ""
)
