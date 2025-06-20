package com.agusteam.caribeando.presenter.shopping.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.GetCommentsTripUseCase
import com.agusteam.caribeando.domain.usecase.GetTripDetailsUseCase
import com.agusteam.caribeando.domain.usecase.MarkFavoriteTripUseCase
import com.agusteam.caribeando.domain.usecase.UnmarkedFavoriteTripUseCase
import com.agusteam.caribeando.presenter.home.navigation.TripDetailScreenRoute
import com.agusteam.caribeando.presenter.shopping.state.TripDetailState
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ShoppingItemDetailsViewModel(
    val logger: CrashReporter,
    val getTripDetails: GetTripDetailsUseCase,
    val markFavoriteTripUseCase: MarkFavoriteTripUseCase,
    val unmarkedFavoriteTripUseCase: UnmarkedFavoriteTripUseCase,
    val getCommentsTripUseCase: GetCommentsTripUseCase
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
                        businessId = model.businessId,
                        businessName = model.businessName,
                        lat = model.lat.toDouble(),
                        lng = model.lng.toDouble(),
                        tripId = model.tripId,
                        images = listOf(),
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

    private suspend fun getTripDetails() {
        if (state.value.tripId.isNotBlank()) {
            setState { copy(isLoadingContent = true) }
            when (val result = getTripDetails(state.value.tripId)) {
                is OperationResult.Success -> {
                    val model = result.data
                    updateState {
                        copy(
                            itemProviderState = itemProviderState.copy(
                                businessImage = model.businessImage
                            ),
                            cancellationPolicy = model.cancellationPolicy,
                            description = model.description,
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
                            tripId = event.tripId,
                            galleryPhotos = listOf(),
                            isMarkedAsFavorite = event.isFavorite,
                            itemProviderState = itemProviderState.copy(
                                month = event.month,
                                businessName = event.businessName,
                            ),
                            businessId = event.businessId,
                            lat = event.lat,
                            lng = event.lng,
                            title = event.name,
                            destiny = event.destiny,
                            arrivingTime = event.arrivingTime,
                            leavingTime = event.leavingTime,
                            meetingPoint = event.meetingPoint,
                            initialPayment = event.initialPrice,
                            totalPayment = event.price
                        )
                    }
                    getTripDetails()
                    when (val result = getCommentsTripUseCase(event.tripId)) {
                        is OperationResult.Error -> {
                            logger.recordException(result.exception)
                        }

                        is OperationResult.Success -> {
                            setState { copy(comments = result.data) }
                        }
                    }
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
                logger.recordException(result.exception)
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
            val businessName: String,
            val month: Int,
            val isFavorite: Boolean,
            val images: List<String>,
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
