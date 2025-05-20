package com.agusteam.caribeando.presenter.wishlist.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.usecase.GetTripFavoriteListUseCase
import com.agusteam.caribeando.presenter.wishlist.state.WishListState
import kotlinx.coroutines.launch

class WishListItemViewModel(
    val getTripFavoriteListUseCase: GetTripFavoriteListUseCase,
) :
    GenericViewModel<WishListState, WishListItemViewModel.WishListEvent>(WishListState()) {

    init {
        viewModelScope.launch {
            getFavoriteTrips()
        }
    }

    fun handleEvent(event: WishListEvent) {
        viewModelScope.launch {
            if (event is WishListEvent.RefreshContent) {
                setState { copy(isRefreshing = true) }
                getFavoriteTrips()
                setState { copy(isRefreshing = false) }
            }
        }
    }

    private suspend fun getFavoriteTrips() {
        setState { copy(isLoading = true) }
        when (val result = getTripFavoriteListUseCase()) {
            is OperationResult.Error -> {
                setState { copy(errorState = true) }
            }

            is OperationResult.Success -> {
                setState { copy(favoriteItems = result.data) }
            }
        }
        setState { copy(isLoading = false) }
    }


    sealed interface WishListEvent {
        data object RefreshContent : WishListEvent
    }
}