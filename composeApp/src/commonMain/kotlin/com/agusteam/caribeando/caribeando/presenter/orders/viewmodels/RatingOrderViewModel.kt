package com.agusteam.caribeando.presenter.shopping.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.RatingOrderUseCase
import com.agusteam.caribeando.presenter.shopping.state.ModalType
import kotlinx.coroutines.launch

class RatingOrderViewModel(private val ratingOrderUseCase: RatingOrderUseCase) :
    GenericViewModel<RatingOrderViewModel.RatingOrderState, RatingOrderViewModel.RatingOrderEvents>(
        RatingOrderState(rating = 0, comment = "")
    ) {
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

    fun handlerEvent(event: RatingOrderEvents) {
        viewModelScope.launch {
            when (event) {

                is RatingOrderEvents.ErrorCleared -> {
                    onErrorHappened(false)
                    if (event.modalType == ModalType.SUCCESS) {
                        sendEvent(RatingOrderEvents.RatedOrder)
                    }
                }

                is RatingOrderEvents.OnOrderLoad -> {
                    setState { copy(orderId = event.orderId) }
                }

                is RatingOrderEvents.OnRatingChanged -> {
                    setRating(event.rating)
                }

                is RatingOrderEvents.OnCommentChanged -> {
                    setComments(event.comment)
                }

                is RatingOrderEvents.CommentOrder -> {
                    val result = ratingOrderUseCase(
                        rating = state.value.rating,
                        comment = state.value.comment,
                        orderId = state.value.orderId
                    )
                    when (result) {
                        is OperationResult.Success -> {
                            onErrorHappened(
                                true,
                                "¡Gracias por tu evaluación!",
                                "Tu reseña ha sido enviada exitosamente. Agradecemos tus comentarios sobre la orden."
                            )
                            setState { copy(modalType = ModalType.SUCCESS) }
                        }

                        is OperationResult.Error -> {
                            setState { copy(modalType = ModalType.ERROR) }
                            onErrorHappened(
                                true,
                                "Error al enviar la reseña",
                                "No se pudo enviar tu evaluación. Por favor, intenta nuevamente más tarde."
                            )
                        }
                    }
                }

                RatingOrderEvents.RatedOrder -> {

                }
            }
        }
    }


    private suspend fun setRating(rating: Int) {
        setState { copy(rating = rating) }
    }

    private suspend fun setComments(comment: String) {
        setState { copy(comment = comment) }
    }

    sealed interface RatingOrderEvents {
        data class OnRatingChanged(val rating: Int) : RatingOrderEvents
        data class OnCommentChanged(val comment: String) : RatingOrderEvents
        data object CommentOrder : RatingOrderEvents
        data object RatedOrder : RatingOrderEvents
        data class OnOrderLoad(val orderId: String) : RatingOrderEvents
        data class ErrorCleared(val modalType: ModalType) : RatingOrderEvents


    }

    data class RatingOrderState(
        val rating: Int,
        val orderId: String = "",
        val comment: String,
        val errorModel: ErrorModel? = null,
        val isLoading: Boolean = false,
        val modalType: ModalType = ModalType.SUCCESS
    ) : ViewModelState {
        fun isValid() = rating >= 0 && comment.isNotBlank()
    }
}