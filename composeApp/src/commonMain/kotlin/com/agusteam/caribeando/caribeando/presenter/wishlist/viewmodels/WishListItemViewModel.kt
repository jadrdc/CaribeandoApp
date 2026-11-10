package com.agusteam.caribeando.presenter.wishlist.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.usecase.GetTripFavoriteListUseCase
import com.agusteam.caribeando.domain.usecase.UnmarkedFavoriteTripUseCase
import com.agusteam.caribeando.presenter.wishlist.state.WishListState
import kotlinx.coroutines.launch

class WishListItemViewModel(
    val logger: CrashReporter,
    val getTripFavoriteListUseCase: GetTripFavoriteListUseCase,
    // 2026-08-20 (design handoff pass, part 2): screen 26's trailing heart circle removes the
    // item directly from the list -- reuses the same use case `ShoppingItemDetailsViewModel`
    // already calls for its favorite toggle, rather than a second implementation.
    val unmarkedFavoriteTripUseCase: UnmarkedFavoriteTripUseCase,
) :
    GenericViewModel<WishListState, WishListItemViewModel.WishListEvent>(WishListState()) {

    init {
        viewModelScope.launch {
            getFavoriteTrips()
        }
    }

    fun handleEvent(event: WishListEvent) {
        viewModelScope.launch {
            when (event) {
                is WishListEvent.RefreshContent -> {
                    setState { copy(isRefreshing = true) }
                    getFavoriteTrips()
                    setState { copy(isRefreshing = false) }
                }

                is WishListEvent.RemoveFavorite -> {
                    // Optimistic: the row disappears immediately (matches the spec's "trailing
                    // circle... removes the item" framing of an instant action). Rolled back on
                    // a real failure so the list doesn't silently drift from the server.
                    val previousItems = state.value.favoriteItems
                    setState { copy(favoriteItems = favoriteItems.filterNot { it.id == event.tripId }) }
                    val result = unmarkedFavoriteTripUseCase(tripId = event.tripId)
                    if (result is OperationResult.Error) {
                        logger.recordException(result.exception)
                        setState { copy(favoriteItems = previousItems) }
                    }
                }
            }
        }
    }

    private suspend fun getFavoriteTrips() {
        setState { copy(isLoading = true) }
        when (val result = getTripFavoriteListUseCase()) {
            is OperationResult.Error -> {
                logger.recordException(result.exception)
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
        data class RemoveFavorite(val tripId: String) : WishListEvent
    }
}
