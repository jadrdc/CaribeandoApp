package com.agusteam.caribeando.presenter.explore.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.models.TripModel

data class ExploreState(
    val errorModel: ErrorModel? = null,
    val categoryState: CategoryState = CategoryState(),
    val items: List<TripModel> = listOf(),
    val shouldBottomModal: Boolean = false,
    val isLoading: Boolean = false,
    val filterState: ExploreFilterState = ExploreFilterState(selectedCategoryModel = null),
    val showUIError: Boolean = false,
    val isRefreshing: Boolean = false
) : ViewModelState
