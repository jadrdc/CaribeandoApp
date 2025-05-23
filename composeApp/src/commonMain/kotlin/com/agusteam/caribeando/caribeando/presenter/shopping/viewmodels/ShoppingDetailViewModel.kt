package com.agusteam.caribeando.presenter.shopping.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.GetTripsIncludedServicesUseCase
import com.agusteam.caribeando.domain.usecase.MarkFavoriteTripUseCase
import com.agusteam.caribeando.domain.usecase.UnmarkedFavoriteTripUseCase
import com.agusteam.caribeando.presenter.home.navigation.TripDetailScreenRoute
import com.agusteam.caribeando.presenter.shopping.state.TripDetailState
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ShoppingItemDetailsViewModel(
    val getTripsIncludedServicesUseCase: GetTripsIncludedServicesUseCase,
    val markFavoriteTripUseCase: MarkFavoriteTripUseCase,
    val unmarkedFavoriteTripUseCase: UnmarkedFavoriteTripUseCase,
) : GenericViewModel<TripDetailState, ShoppingItemDetailsViewModel.ShoppingDetailEvent>(
    TripDetailState()
) {

    fun loadShoppingDetails(model: TripDetailScreenRoute) {
        viewModelScope.launch {
            try {
                handleEvent(
                    ShoppingDetailEvent.ShoppingDetailLoaded(
                        isFavorite = model.isFavorite,
                        name = model.name,
                        month = model.month,
                        businessImage = model.businessImage,
                        businessId = model.businessId,
                        businessName = model.businessName,
                        description = model.description,
                        lat = model.lat.toDouble(),
                        lng = model.lng.toDouble(),
                        tripId = model.tripId,
                        images = model.images,
                        cancellationPolicy = model.cancellationPolicy,
                        initialPrice = model.initialPayment,
                        meetingPoint = model.meetingPoint,
                        leavingTime = model.leavingTime,
                        arrivingTime = model.arrivingTime,
                        price = model.price,
                        destiny = model.destiny,
                        tripScheduleId = model.tripScheduleId
                    )
                )
            } catch (e: CancellationException) {
                val es = e.message
            }
        }
    }

    private suspend fun getIncludedServices() {
        if (state.value.tripId.isNotBlank()) {
            setState { copy(isLoadingContent = true) }
            when (val result = getTripsIncludedServicesUseCase(state.value.tripId)) {
                is OperationResult.Success -> {
                    updateState { copy(includedServices = result.data) }
                }

                is OperationResult.Error -> {
                    val e = result.exception
                }
            }
            setState { copy(isLoadingContent = false) }
        }
    }

    fun handleEvent(event: ShoppingDetailEvent) {
        viewModelScope.launch {
            when (event) {
                is ShoppingDetailEvent.MarkFavorite -> {
                    markTripFavorite()
                }

                is ShoppingDetailEvent.OnErrorModalAccepted -> {
                    onErrorHappened(false)
                }

                is ShoppingDetailEvent.ShoppingDetailLoaded -> {
                    setState {
                        copy(
                            galleryPhotos = event.images,
                            tripId = event.tripId,
                            isMarkedAsFavorite = event.isFavorite,
                            itemProviderState = itemProviderState.copy(
                                month = event.month,
                                businessName = event.businessName,
                                businessImage = event.businessImage
                            ),
                            businessId = event.businessId,
                            lat = event.lat,
                            lng = event.lng,
                            description = event.description,
                            title = event.name,
                            cancellationPolicy = event.cancellationPolicy,
                            destiny = event.destiny,
                            arrivingTime = event.arrivingTime,
                            leavingTime = event.leavingTime,
                            meetingPoint = event.meetingPoint,
                            initialPayment = event.initialPrice,
                            totalPayment = event.price
                        )
                    }
                    getIncludedServices()

                }

            }
        }
    }

    private suspend fun markTripFavorite() {
        setState { copy(isLoading = true) }
        val markState = !state.value.isMarkedAsFavorite
        val result = if (markState) {
            markFavoriteTripUseCase(tripId = state.value.tripId)
        } else {
            unmarkedFavoriteTripUseCase(
                tripId = state.value.tripId
            )
        }
        when (result) {
            is OperationResult.Error -> {
                onErrorHappened(
                    true,
                    "Error cambiando el estado de viaje",
                    "No se pudo completar la operacion,intente mas tarde."
                )
            }

            is OperationResult.Success -> {
                setState {
                    copy(
                        isMarkedAsFavorite = markState
                    )
                }
            }
        }
        setState { copy(isLoading = false) }
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

    sealed interface ShoppingDetailEvent {
        class ShoppingDetailLoaded(
            val tripId: String,
            val name: String,
            val lat: Double,
            val lng: Double,
            val businessId: String,
            val businessImage: String,
            val businessName: String,
            val description: String,
            val month: Int,
            val isFavorite: Boolean,
            val images: List<String>,
            val cancellationPolicy: String,
            val arrivingTime: String = "",
            val leavingTime: String = "",
            val meetingPoint: String = "",
            val destiny: String = "",
            val price: Int,
            val initialPrice: Int = 0,
            val tripScheduleId: String,
        ) : ShoppingDetailEvent

        data object MarkFavorite : ShoppingDetailEvent
        data object OnErrorModalAccepted : ShoppingDetailEvent
    }

}
