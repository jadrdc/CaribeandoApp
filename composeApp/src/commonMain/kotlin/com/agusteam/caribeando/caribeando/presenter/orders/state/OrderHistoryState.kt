package com.agusteam.caribeando.presenter.orders.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.UpcomingOrders

data class OrderHistoryState(
    val isRefreshing: Boolean = false,
    val upcomingTripSkeletonView: Boolean = false,
    val oldItemsTripSkeletonView: Boolean = false,
    val upcomingItems: List<UpcomingOrders> = listOf(),
    val oldItems: List<UpcomingOrders> = listOf()
) : ViewModelState
