package com.agusteam.caribeando.presenter.shopping.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.ReportOrderUseCase
import com.agusteam.caribeando.presenter.shopping.state.ModalType
import com.agusteam.caribeando.presenter.shopping.state.ReportOrderState
import kotlinx.coroutines.launch

class ReportOrderViewModel(val useCase: ReportOrderUseCase) :
    GenericViewModel<ReportOrderState, ReportOrderViewModel.ReportOrderEvent>(ReportOrderState()) {


    fun handlerEvent(event: ReportOrderEvent) {
        viewModelScope.launch {
            when (event) {
                is ReportOrderEvent.ReportOrder -> {
                    setState { copy(isLoading = true) }
                    val result = useCase(state.value.orderId, state.value.message)
                    when (result) {
                        is OperationResult.Success -> {
                            onErrorHappened(
                                true,
                                "Orden reportada",
                                "Se ha reportado un inconveniente con la orden por parte del usuario. Nuestro equipo está en ello y nos comunicaremos contigo para ofrecer una solución."
                            )
                            setState { copy(modalType = ModalType.SUCCESS) }
                        }

                        is OperationResult.Error -> {
                            setState { copy(modalType = ModalType.ERROR) }
                            onErrorHappened(
                                true,
                                "Error reportando orden",
                                "No se pudo enviar el reporte. Intenta nuevamente más tarde."
                            )
                        }
                    }
                    setState { copy(isLoading = false) }
                }

                is ReportOrderEvent.OnOrderLoad -> {
                    setState { copy(orderId = event.order) }
                }

                is ReportOrderEvent.OnMessageChanged -> {
                    setState { copy(message = event.message) }
                }

                is ReportOrderEvent.ErrorCleared -> {
                    onErrorHappened(false)
                    if (event.modalType == ModalType.SUCCESS) {
                        updateState { copy(message = "") }
                        sendEvent(ReportOrderEvent.OrderReportedSuccefull)
                    }
                }

                else -> {}
            }
        }
    }

    private suspend fun onErrorHappened(value: Boolean, title: String = "", message: String = "") {
        val errorModel = if (!value) {
            null
        } else {
            ErrorModel(title = title, message = message)
        }
        setState {
            copy(
                errorModel = errorModel
            )
        }
    }

    sealed interface ReportOrderEvent {
        data object OrderReportedSuccefull : ReportOrderEvent
        data class ErrorCleared(val modalType: ModalType) : ReportOrderEvent
        data object ReportOrder : ReportOrderEvent
        data object OrderReported : ReportOrderEvent
        data class OnOrderLoad(val order: String) : ReportOrderEvent
        data class OnMessageChanged(val message: String) : ReportOrderEvent
    }
}