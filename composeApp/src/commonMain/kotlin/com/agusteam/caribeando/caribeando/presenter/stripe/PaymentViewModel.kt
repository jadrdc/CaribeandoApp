package com.agusteam.caribeando.presenter.stripe

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
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
import com.agusteam.caribeando.domain.usecase.ProcessSuccessPaymentOrderUseCase
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
                    onErrorHappened(true, events.title, events.reason)
                    val result = cancelPaymentOrderUseCase(
                        state.value.orderId,
                        events.reason
                    )
                    if (result is OperationResult.Error) {
                        logger.recordException(result.exception)
                    }
                }

                is PaymentEvents.StripePaymentSucess -> {
                    processSuccessPaymentOrderUseCase(
                        state.value.orderId,
                        state.value.stripeState?.paymentIntentId.orEmpty()
                    )
                }

                is PaymentEvents.SetErrorMessage -> {
                    onErrorHappened(value = true, events.title, events.message)
                }

                is PaymentEvents.ConfigureStripe -> {
                    startStripeUseCase.setConfig(events.config)
                }

                is PaymentEvents.OnErrorModalAccepted -> {
                    onErrorHappened(false)
                }

                is PaymentEvents.InitialLoad -> {
                    setState {
                        copy(
                            title = events.title.orEmpty(),
                            destiny = events.destiny,
                            leavingTime = events.leavingTime.orEmpty(),
                            meetingPoint = events.meetingPoint.orEmpty(),
                            initialPayment = events.initialPayment,
                            totalPayment = events.totalPayment,
                            tripDetailId = events.tripDetailId.orEmpty(),
                        )
                    }
                    getTripDetails(events.tripId)
                }

                is PaymentEvents.OnPaymentTypePicked -> {
                    changePaymentModel(events.paymentModel)
                }

                is PaymentEvents.OnStripePaymentStart -> {
                    updateState { copy(isLoading = true) }
                    val amount = if (state.value.selectedPaymentType == PaymentModel.TOTAL_PAYMENT)
                        state.value.totalPayment else state.value.initialPayment

                    val name = listOfNotNull(
                        state.value.fullName.takeIf { it.isNotBlank() },
                        state.value.title.takeIf { it.isNotBlank() },
                        state.value.leavingTime.takeIf { it.isNotBlank() }
                    ).joinToString(" ")

                    when (val result = getStripePaymentIntentUseCase(amount, name)) {
                        is OperationResult.Error -> {
                            logger.recordException(result.exception)
                            onErrorHappened(
                                true,
                                "Error en el proceso de pago",
                                "No pudimos completar tu transacción en este momento. Por favor, verifica la información e inténtalo nuevamente"
                            )
                        }

                        is OperationResult.Success -> {
                            val stripe = result.data
                            setState { copy(stripeState = stripe) }
                            startStripeUseCase.startStripe(stripe)

                            val tripId = state.value.tripDetailId.takeIf { it.isNotBlank() }
                            if (tripId != null) {
                                val orderResult = createPendingPaymentOrderUseCase(tripId)
                                when (orderResult) {
                                    is OperationResult.Error -> {
                                        logger.recordException(orderResult.exception)
                                        onErrorHappened(
                                            true,
                                            "Error en el proceso de pago",
                                            "No pudimos completar tu transacción. Intenta más tarde."
                                        )
                                    }

                                    is OperationResult.Success -> {
                                        setState { copy(orderId = orderResult.data.orderId) }
                                        startStripeUseCase.presentPaymentSheet()
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
                        galleryPhoto = model.images, profilePhoto = model.images.firstOrNull() ?: ""
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
    val selectedPaymentType: PaymentModel = PaymentModel.TOTAL_PAYMENT,
    val errorModel: ErrorModel? = null,
    val tripDetailId: String = "",
    val orderId: String = "",
    val galleryPhoto: List<String> = emptyList(),
    val businessPhoto: String = "",
    val itemProviderState: ItemProviderState = ItemProviderState()
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
        val galleryPhoto: List<String> = emptyList()
    ) : PaymentEvents

    data class SetErrorMessage(val title: String, val message: String) : PaymentEvents
    data object OnErrorModalAccepted : PaymentEvents
    data object OnStripePaymentStart : PaymentEvents
    data class OnPaymentTypePicked(val paymentModel: PaymentModel) : PaymentEvents
    data class ConfigureStripe(val config: StripeConfiguration) : PaymentEvents
    data object StripePaymentSucess : PaymentEvents
    data class FailedPayment(val title: String, val reason: String) : PaymentEvents
}

data class ItemProviderState(
    val businessImage: String = ""
)
