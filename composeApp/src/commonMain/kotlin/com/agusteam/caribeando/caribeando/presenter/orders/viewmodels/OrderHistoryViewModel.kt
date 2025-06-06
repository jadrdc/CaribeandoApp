package com.agusteam.caribeando.presenter.orders.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.usecase.GetPastTripOrderUseCase
import com.agusteam.caribeando.domain.usecase.GetUpcomingTripOrderUseCase
import com.agusteam.caribeando.presenter.orders.state.OrderHistoryState
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val logger: CrashReporter,
    private val useCase: GetUpcomingTripOrderUseCase,
    private val getPastTripOrderUseCase: GetPastTripOrderUseCase
) :
    GenericViewModel<OrderHistoryState, OrderHistoryViewModel.OrderHistoryEvent>(OrderHistoryState()) {

    init {
        viewModelScope.launch {
            getUpcomingTrips()
            getPastTrip()
        }
    }


    fun handlerEvent(event: OrderHistoryEvent) {
        viewModelScope.launch {
            if (event is OrderHistoryEvent.RefreshContent) {
                refresh()
            }
        }
    }

    private suspend fun refresh() {
        setState { copy(isRefreshing = true) }
        getUpcomingTrips()
        getPastTrip()
        setState { copy(isRefreshing = false) }
    }

    private suspend fun getUpcomingTrips() {
        setState { copy(upcomingTripSkeletonView = true, oldItemsTripSkeletonView = true) }
        when (val result = useCase()) {
            is OperationResult.Error -> {
                logger.recordException(result.exception)
            }

            is OperationResult.Success -> {
                setState { copy(upcomingItems = result.data) }
            }
        }
        setState { copy(upcomingTripSkeletonView = false) }
    }

    private suspend fun getPastTrip() {
        when (val result = getPastTripOrderUseCase()) {
            is OperationResult.Error -> {
                logger.recordException(result.exception)
            }

            is OperationResult.Success -> {
                setState { copy(oldItems = result.data) }
            }
        }
        setState { copy(oldItemsTripSkeletonView = false) }
    }

    sealed interface OrderHistoryEvent {
        data object RefreshContent : OrderHistoryEvent
    }
}