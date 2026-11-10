package com.agusteam.caribeando.presenter.orders.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.caribeando.presenter.common.EmptyState
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.common.PullToRefreshContainer
import com.agusteam.caribeando.presenter.common.Toast
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect
import com.agusteam.caribeando.presenter.orders.composable.PreviousTripItemSection
import com.agusteam.caribeando.presenter.orders.composable.UpcomingOrderTripItemSection
import com.agusteam.caribeando.presenter.orders.viewmodels.OrderHistoryViewModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.explore_trips_action
import caribeando.composeapp.generated.resources.no_trips_subtitle
import caribeando.composeapp.generated.resources.no_trips_title
import caribeando.composeapp.generated.resources.old
import caribeando.composeapp.generated.resources.rate_success_toast
import caribeando.composeapp.generated.resources.trips_title
import caribeando.composeapp.generated.resources.upcoming

/**
 * The handoff's "18. Próximos viajes" / "23. Viajes pasados" / "25. Viajes — vacío" all live on
 * this one screen: a title, a two-tab switch ("Próximos viajes" / "Viajes pasados"), and each
 * tab's list or empty state.
 *
 * 2026-08-20 (design handoff pass): this screen previously had NO tab UI at all -- both sections
 * were always rendered together, gated by a tangle of boolean OR conditions that always evaluated
 * true whenever either list was non-empty. [selectedTab] is new, screen-local UI state (not
 * threaded into `OrderHistoryViewModel`/`OrderHistoryState`, which don't need to know which tab is
 * showing) that now actually switches which section renders.
 *
 * [showRatedToast]/[onRatedToastShown] let the "24. Calificar viaje" flow report a completed
 * review back to this screen: the handoff's copy is "success returns to Viajes pasados with toast
 * 'Gracias por tu reseña' and the row flips to rated" -- flipping the row requires re-fetching
 * (there's no client-side toggle for `hasBeenEvaluated`), so this also triggers one
 * `RefreshContent` when the toast fires.
 */
@Composable
fun OrderHistoryScreen(
    viewModel: OrderHistoryViewModel = koinViewModel(),
    // third param: isUpcoming — true for the upcoming-trips section, false for past trips.
    goDetails: (UpcomingOrders, Boolean, Boolean) -> Unit,
    rateOrderTrip: (UpcomingOrders) -> Unit,
    onExploreClick: () -> Unit = {},
    showRatedToast: Boolean = false,
    onRatedToastShown: () -> Unit = {},
    // 2026-08-20 (re-verification pass): screen 21's cancel-confirmation flow used to show a
    // modal "result" dialog on a successful cancellation, but the handoff's screen-21 spec never
    // describes a second result screen at all -- it just wants confirming to return here with a
    // toast, the same pattern as [showRatedToast] above. The message itself is dynamic (the
    // backend's own cancellation-result copy), so this carries the String directly rather than a
    // fixed toast string resource.
    cancelledToastMessage: String? = null,
    onCancelledToastShown: () -> Unit = {},
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    val ratedSuccessMessage = stringResource(Res.string.rate_success_toast)
    LaunchedEffect(showRatedToast) {
        if (showRatedToast) {
            toastMessage = ratedSuccessMessage
            viewModel.handlerEvent(OrderHistoryViewModel.OrderHistoryEvent.RefreshContent)
            onRatedToastShown()
        }
    }
    LaunchedEffect(cancelledToastMessage) {
        if (cancelledToastMessage != null) {
            toastMessage = cancelledToastMessage
            viewModel.handlerEvent(OrderHistoryViewModel.OrderHistoryEvent.RefreshContent)
            onCancelledToastShown()
        }
    }

    val everythingEmpty = !state.upcomingTripSkeletonView && !state.oldItemsTripSkeletonView &&
        state.upcomingItems.isEmpty() && state.oldItems.isEmpty()

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Column(Modifier.padding(horizontal = 20.dp).padding(top = 24.dp)) {
                Text(
                    text = stringResource(Res.string.trips_title),
                    color = colors.textPrimary,
                    style = textStyles.screenTitleTabRoot,
                )
                if (!everythingEmpty) {
                    Row(
                        modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(22.dp)
                    ) {
                        TripsTab(
                            text = stringResource(Res.string.upcoming),
                            isSelected = selectedTab == 0,
                        ) { selectedTab = 0 }
                        TripsTab(
                            text = stringResource(Res.string.old),
                            isSelected = selectedTab == 1,
                        ) { selectedTab = 1 }
                    }
                    Box(
                        Modifier
                            .padding(top = 11.dp)
                            .fillMaxWidth()
                            .background(colors.hairline)
                            .height(1.dp)
                    )
                }
            }

            if (everythingEmpty) {
                EmptyState(
                    modifier = Modifier.weight(1f),
                    message = stringResource(Res.string.no_trips_title),
                    subtitle = stringResource(Res.string.no_trips_subtitle),
                    actionText = stringResource(Res.string.explore_trips_action),
                    onAction = onExploreClick,
                )
            } else {
                PullToRefreshContainer(
                    modifier = Modifier.weight(1f),
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.handlerEvent(OrderHistoryViewModel.OrderHistoryEvent.RefreshContent) }
                ) {
                    LazyColumn(Modifier.padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 24.dp)) {
                        if (selectedTab == 0) {
                            if (state.upcomingTripSkeletonView) {
                                item { ShimmerUpcomingTripSkeleton() }
                            } else if (state.upcomingItems.isEmpty()) {
                                item {
                                    EmptyState(
                                        modifier = Modifier.height(320.dp),
                                        message = stringResource(Res.string.no_trips_title),
                                        subtitle = stringResource(Res.string.no_trips_subtitle),
                                    )
                                }
                            } else {
                                item {
                                    UpcomingOrderTripItemSection(
                                        upcomingTripItemList = state.upcomingItems,
                                        goDetails = { item -> goDetails(item, true, true) })
                                }
                            }
                        } else {
                            if (state.oldItemsTripSkeletonView) {
                                item { ShimmerPreviousTripSkeleton() }
                            } else if (state.oldItems.isEmpty()) {
                                item {
                                    EmptyState(
                                        modifier = Modifier.height(320.dp),
                                        message = stringResource(Res.string.no_trips_title),
                                        subtitle = stringResource(Res.string.no_trips_subtitle),
                                    )
                                }
                            } else {
                                item {
                                    PreviousTripItemSection(
                                        modifier = Modifier,
                                        oldItems = state.oldItems,
                                        goDetails = { item -> goDetails(item, item.hasBeenEvaluated, false) },
                                        rateOrderTrip = { item -> rateOrderTrip(item) })
                                }
                            }
                        }
                    }
                }
            }
        }
        Toast(message = toastMessage, onDismissed = { toastMessage = null })
    }
}

@Composable
private fun TripsTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .bounceClick(indication = null, scaleDown = 0.97f) { onClick() }
    ) {
        // 2026-08-20 (screens 18-30 fidelity pass): inactive tab label was `colors.grey500`
        // (#757575) -- spec wants #5F6A70 (`textSecondary`) for this label.
        Text(
            text = text,
            color = if (isSelected) colors.textPrimary else colors.textSecondary,
            style = if (isSelected) textStyles.tabActive else textStyles.tabInactive,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(2.dp)
                .width(if (isSelected) 100.dp else 0.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(if (isSelected) colors.redFill else Color.Transparent)
        )
    }
}

@Composable
private fun ShimmerUpcomingTripSkeleton() {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp).padding(top = 4.dp)
                .clip(RoundedCornerShape(18.dp)).shimmerEffect()
        )
    }
}

@Composable
private fun ShimmerPreviousTripSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 4.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).shimmerEffect()
                    .height(66.dp)
            )
        }
    }
}
