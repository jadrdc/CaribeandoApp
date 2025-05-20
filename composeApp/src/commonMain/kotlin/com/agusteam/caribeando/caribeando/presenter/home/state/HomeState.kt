package com.agusteam.caribeando.presenter.home.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel

data class HomeState(
    val currentNavigationOption: HomeOption = HomeOption.EXPLORE,
    val isLoading: Boolean = false,
    val errorModel: ErrorModel? = null
) : ViewModelState

enum class HomeOption {
    EXPLORE, FAVORITE, TRIP, PROFILE, SHOPPING_ITEM_DETAIL, WISHLIST
}