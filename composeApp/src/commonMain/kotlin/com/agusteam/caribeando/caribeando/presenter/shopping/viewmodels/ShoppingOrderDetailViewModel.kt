package com.agusteam.caribeando.presenter.shopping.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.usecase.RequestOrderCancellationUseCase
import com.agusteam.caribeando.presenter.shopping.state.ShoppingOrderDetailState
import kotlinx.coroutines.launch

class ShoppingOrderDetailViewModel(
    private val requestOrderCancellationUseCase: RequestOrderCancellationUseCase,
    private val logger: CrashReporter
) : GenericViewModel<ShoppingOrderDetailState, ShoppingOrderDetailEvent>(ShoppingOrderDetailState()) {

    fun handleEvent(event: ShoppingOrderDetailEvent) {
        when (event) {
            is ShoppingOrderDetailEvent.ShowCancelConfirmation -> updateState { copy(showCancelConfirmation = true) }
            is ShoppingOrderDetailEvent.DismissCancelConfirmation -> updateState { copy(showCancelConfirmation = false) }
            is ShoppingOrderDetailEvent.ConfirmCancel -> confirmCancel(event.orderId)
            is ShoppingOrderDetailEvent.DismissCancelResult -> updateState {
                copy(cancelResultMessage = null, cancelSucceeded = false)
            }
        }
    }

    private fun confirmCancel(orderId: String) {
        viewModelScope.launch {
            updateState { copy(showCancelConfirmation = false, isCancelling = true) }
            when (val result = requestOrderCancellationUseCase(orderId)) {
                is OperationResult.Error -> {
                    logger.recordException(result.exception)
                    updateState {
                        copy(
                            isCancelling = false,
                            cancelResultMessage = "No pudimos cancelar esta reserva. Intenta de nuevo o contacta soporte.",
                            cancelSucceeded = false
                        )
                    }
                }

                is OperationResult.Success -> {
                    updateState {
                        copy(
                            isCancelling = false,
                            cancelResultMessage = result.data.message,
                            cancelSucceeded = true
                        )
                    }
                }
            }
        }
    }
}

sealed interface ShoppingOrderDetailEvent {
    data object ShowCancelConfirmation : ShoppingOrderDetailEvent
    data object DismissCancelConfirmation : ShoppingOrderDetailEvent
    data class ConfirmCancel(val orderId: String) : ShoppingOrderDetailEvent
    data object DismissCancelResult : ShoppingOrderDetailEvent
}
