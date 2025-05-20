package com.agusteam.caribeando.presenter.profile.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.usecase.GetTripProviderDetailsUseCase
import com.agusteam.caribeando.domain.usecase.GetUpcomingTripByProviderUseCase
import com.agusteam.caribeando.presenter.profile.state.TripProviderState
import kotlinx.coroutines.launch

class TripProviderViewModel(
    private val getTripProviderDetailsUseCase: GetTripProviderDetailsUseCase,
    val getUpcomingTripByProviderUseCase: GetUpcomingTripByProviderUseCase,
) :
    GenericViewModel<TripProviderState, TripProviderViewModel.TripProviderEvent>(TripProviderState()) {


    fun handlerEvent(event: TripProviderEvent) {
        viewModelScope.launch {
            when (event) {
                is TripProviderEvent.TripProviderDetailsLoading -> {
                    setState { copy(isLoading = true) }
                    getDetails(event.id)
                    setState { copy(isLoading = false) }
                }
            }
        }
    }

    private suspend fun getDetails(id: String) {
        when (val result = getTripProviderDetailsUseCase(id)) {
            is OperationResult.Error -> {

            }

            is OperationResult.Success -> {
                updateState { copy(tripProviderModel = result.data) }
                getUpcomingTrips(id)
            }
        }
    }

    private suspend fun getUpcomingTrips(id: String) {
        when (val result = getUpcomingTripByProviderUseCase(id)) {
            is OperationResult.Error -> {

            }

            is OperationResult.Success -> {
                updateState { copy(upcomingTrips = result.data) }
            }
        }
    }


    sealed interface TripProviderEvent {
        class TripProviderDetailsLoading(val id: String) : TripProviderEvent
    }

}