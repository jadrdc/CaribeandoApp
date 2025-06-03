package com.agusteam.caribeando.presenter.wishlist.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.models.TripModel

data class WishListState(
    val errorModel: ErrorModel? = null,
    val favoriteItems: List<TripModel> = listOf(),
    val isLoading: Boolean = false,
    val errorState: Boolean = false,
    val isRefreshing: Boolean = false,
) : ViewModelState
