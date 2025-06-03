package com.agusteam.caribeando.presenter.profile.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.domain.models.TripProviderModel

data class TripProviderState(
    val isLoading: Boolean = true,
    val tripProviderModel: TripProviderModel? = null,
    val upcomingTrips: List<TripModel> = listOf()
) : ViewModelState
