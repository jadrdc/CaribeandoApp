package com.agusteam.caribeando.presenter.orders.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.usecase.GetTripDetailsUseCase
import com.agusteam.caribeando.domain.usecase.GetTripsIncludedServicesUseCase
import com.agusteam.caribeando.presenter.wishlist.state.WishListOrderDetailState
import kotlinx.coroutines.launch

class WishListOrderDetailViewModel(
    private val logger: CrashReporter,
    val getTripDetailsUseCase: GetTripDetailsUseCase,
) : GenericViewModel<WishListOrderDetailState, WishListOrderDetailViewModel.OrderDetailsEvent>(
    WishListOrderDetailState()
) {

    fun handleEvent(event: OrderDetailsEvent) {
        viewModelScope.launch {
            when (event) {
                is OrderDetailsEvent.OrderDetailsLoadIncludeServices -> {
                    setState {
                        copy(
                            itemProviderState = itemProviderState.copy(
                                businessName = event.businessName,
                                month = event.month
                            )
                        )
                    }
                    getTripDetails(event.tripId)
                }
            }
        }
    }

    private suspend fun getTripDetails(tripId: String) {
        if (tripId.isNotBlank()) {
            setState { copy(isLoadingContent = true) }
            when (val result = getTripDetailsUseCase(tripId)) {
                is OperationResult.Success -> {
                    updateState {
                        val model = result.data
                        copy(
                            itemProviderState = itemProviderState.copy(
                                businessImage = model.businessImage
                            ),
                            description = model.description,
                            cancellationPolicy = model.cancellationPolicy,
                            galleryPhotos = model.images,
                            includedServices = model.services
                        )
                    }
                }

                is OperationResult.Error -> {
                    logger.recordException(result.exception)

                }
            }
            setState { copy(isLoadingContent = false) }
        }
    }


    sealed interface OrderDetailsEvent {
        data class OrderDetailsLoadIncludeServices(
            val tripId: String,
            val businessName: String,
            val month: Int
        ) : OrderDetailsEvent
    }
}