package com.agusteam.caribeando.presenter.orders.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.caribeando.presenter.common.EmptyState
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.common.PullToRefreshContainer
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect
import com.agusteam.caribeando.presenter.orders.composable.PreviousTripItemSection
import com.agusteam.caribeando.presenter.orders.composable.UpcomingOrderTripItemSection
import com.agusteam.caribeando.presenter.orders.viewmodels.OrderHistoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderHistoryScreen(
    viewModel: OrderHistoryViewModel = koinViewModel(),
    goDetails: (UpcomingOrders, Boolean) -> Unit,
    rateOrderTrip: (String) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    if (!state.upcomingTripSkeletonView && !state.oldItemsTripSkeletonView && state.upcomingItems.isEmpty() && state.oldItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            EmptyState(
                message = "No hay viajes que mostrar", actionText = "Intente nuevamente"
            )

        }
    }

    PullToRefreshContainer(modifier = Modifier, isRefreshing = state.isRefreshing, onRefresh = {
        viewModel.handlerEvent(OrderHistoryViewModel.OrderHistoryEvent.RefreshContent)
    }) {
        LazyColumn(Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {
            if (state.upcomingTripSkeletonView || state.oldItemsTripSkeletonView) {
                item {
                    ShimmerUpcomingTripSkeleton()
                }
                item {
                    ShimmerPreviousTripSkeleton()
                }
            } else {
                if (state.upcomingItems.isNotEmpty() && state.oldItems.isNotEmpty() || (state.upcomingItems.isEmpty() && state.oldItems.isNotEmpty()) ||
                    (state.upcomingItems.isNotEmpty() && state.oldItems.isEmpty())
                ) {
                    item {
                        UpcomingOrderTripItemSection(upcomingTripItemList = state.upcomingItems,
                            goDetails = { item ->
                                goDetails(item, true)
                            })
                    }
                }
                if (state.oldItems.isNotEmpty() && state.upcomingItems.isNotEmpty() || (state.oldItems.isEmpty() && state.upcomingItems.isNotEmpty()) ||
                    (state.oldItems.isNotEmpty() && state.upcomingItems.isEmpty())
                ) {
                    item {
                        PreviousTripItemSection(oldItems = state.oldItems, goDetails = { item ->
                            goDetails(item, item.hasBeenEvaluated)
                        }, rateOrderTrip = { tripScheduled ->
                            rateOrderTrip(tripScheduled)
                        })
                    }
                }
            }

        }
    }
}

@Composable
private fun ShimmerUpcomingTripSkeleton() {
    Column {
        Box(
            modifier = Modifier.padding(top = 4.dp).clip(RoundedCornerShape(8.dp)).height(32.dp)
                .width(100.dp).shimmerEffect()
        )
        Box(
            modifier = Modifier.fillMaxWidth().height(250.dp).padding(top = 16.dp)
                .clip(RoundedCornerShape(16.dp)).shimmerEffect()
        ) {}
    }
}

@Composable
private fun ShimmerPreviousTripSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier.padding(top = 4.dp).clip(RoundedCornerShape(8.dp)).height(32.dp)
                .width(100.dp).shimmerEffect()
        )
        repeat(3) {
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).shimmerEffect()
                    .height(32.dp)
            )
        }
    }
}
