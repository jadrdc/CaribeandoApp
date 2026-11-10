package com.agusteam.caribeando.presenter.shopping.viewmodels

import caribeando.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.getString
import caribeando.composeapp.generated.resources.rating_error_message
import caribeando.composeapp.generated.resources.rating_error_title
import caribeando.composeapp.generated.resources.rating_thanks_message
import caribeando.composeapp.generated.resources.rating_thanks_title
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.RatingOrderUseCase
import com.agusteam.caribeando.presenter.shopping.state.ModalType
import kotlinx.coroutines.launch

class RatingOrderViewModel(
    private val ratingOrderUseCase: RatingOrderUseCase,
    private val logger: CrashReporter
) : GenericViewModel<RatingOrderViewModel.RatingOrderState, RatingOrderViewModel.RatingOrderEvents>(
    RatingOrderState(rating = 0, comment = "")
) {

    fun handlerEvent(event: RatingOrderEvents) {
        viewModelScope.launch {
            when (event) {
                is RatingOrderEvents.ErrorCleared -> {
                    onErrorHappened(false)
                    if (event.modalType == ModalType.SUCCESS) {
                        sendEvent(RatingOrderEvents.RatedOrder)
                    }
                }
                is RatingOrderEvents.OnOrderLoad -> setState { copy(orderId = event.orderId) }
                is RatingOrderEvents.OnRatingChanged -> setRating(event.rating)
                is RatingOrderEvents.OnCommentChanged -> setComments(event.comment)
                is RatingOrderEvents.CommentOrder -> {
                    val current = state.value
                    when (val result = ratingOrderUseCase(current.rating, current.orderId, current.comment)) {
                        is OperationResult.Success -> {
                            onErrorHappened(
                                true,
                                getString(Res.string.rating_thanks_title),
                                getString(Res.string.rating_thanks_message)
                            )
                            setState { copy(modalType = ModalType.SUCCESS) }
                        }
                        is OperationResult.Error -> {
                            setState { copy(modalType = ModalType.ERROR) }
                            logger.recordException(result.exception)
                            onErrorHappened(
                                true,
                                getString(Res.string.rating_error_title),
                                getString(Res.string.rating_error_message)
                            )
                        }
                    }
                }
                is RatingOrderEvents.RatedOrder -> Unit
            }
        }
    }

    private suspend fun onErrorHappened(value: Boolean, title: String = "", message: String = "") {
        val errorModel = if (value) ErrorModel(title, message) else null
        setState { copy(errorModel = errorModel) }
    }

    private suspend fun setRating(rating: Int) {
        setState { copy(rating = rating) }
    }

    private suspend fun setComments(comment: String) {
        setState { copy(comment = comment) }
    }

    data class RatingOrderState(
        val rating: Int,
        val orderId: String = "",
        val comment: String,
        val errorModel: ErrorModel? = null,
        val isLoading: Boolean = false,
        val modalType: ModalType = ModalType.SUCCESS
    ) : ViewModelState {
        val isValid: Boolean get() = rating >= 0 && comment.isNotBlank()
    }

    sealed interface RatingOrderEvents {
        data class OnRatingChanged(val rating: Int) : RatingOrderEvents
        data class OnCommentChanged(val comment: String) : RatingOrderEvents
        data object CommentOrder : RatingOrderEvents
        data object RatedOrder : RatingOrderEvents
        data class OnOrderLoad(val orderId: String) : RatingOrderEvents
        data class ErrorCleared(val modalType: ModalType) : RatingOrderEvents
    }
}
