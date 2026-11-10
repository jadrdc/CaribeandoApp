// ExploreScreen.kt
package com.agusteam.caribeando.presenter.explore.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.domain.models.CategoryModel
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.common.BottomModalSheet
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ErrorState
import com.agusteam.caribeando.presenter.common.PullToRefreshContainer
import com.agusteam.caribeando.presenter.common.SearchBar
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.common.loading.TripItemLoadingSection
import com.agusteam.caribeando.caribeando.presenter.common.EmptyState
import com.agusteam.caribeando.presenter.explore.composable.CategorySection
import com.agusteam.caribeando.presenter.explore.composable.HomeFilterContent
import com.agusteam.caribeando.presenter.explore.composable.TripItem
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreViewModel
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.category_all
import caribeando.composeapp.generated.resources.clear
import caribeando.composeapp.generated.resources.excursions_count
import caribeando.composeapp.generated.resources.explore
import caribeando.composeapp.generated.resources.ic_profile
import caribeando.composeapp.generated.resources.no_results_subtitle
import caribeando.composeapp.generated.resources.no_results_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = koinViewModel(),
    onProfileClick: () -> Unit = {},
    goDetails: (TripModel) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val bottomState = rememberModalBottomSheetState(true)
    val listState = rememberLazyListState()
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    // 2026-10-09 (design handoff, screen 8): the Calendario screen, opened from the filter
    // sheet's "Fechas exactas". Rendered as a full-screen layer above the sheet.
    var showCalendar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onExploreEventChanged(ExploreEvent.InitLoad)
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisible to totalItems
        }
            .distinctUntilChanged()
            .filter { (lastVisible, totalItems) ->
                val isAtEnd = lastVisible >= totalItems - 1
                val canLoadMore = isAtEnd &&
                        !state.value.categoryState.isLoadingSkeleton &&
                        !state.value.isLoading
                canLoadMore
            }
            .collectLatest {
                viewModel.onExploreEventChanged(ExploreEvent.LoadMoreTrips)
            }
    }

    if (state.value.errorModel != null) {
        ErrorModal(
            title = state.value.errorModel?.title ?: "",
            message = state.value.errorModel?.message ?: "",
            showError = true,
            onDismiss = {
                viewModel.onExploreEventChanged(ExploreEvent.OnErrorModalAccepted)
            }
        )
    }

    if (state.value.showUIError) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ErrorState(onRetry = { viewModel.onExploreEventChanged(ExploreEvent.InitLoad) })
        }
    } else {
        // 2026-08-20 (dark-mode audit): was the raw, theme-blind `backGround` val (always
        // white) -- with this screen's text already using theme-reactive `colors.*` roles that
        // flip to light colors in dark mode, a permanently-white background meant near-white
        // text on a white screen. Switched to `colors.pageBackground`.
        Box(modifier = Modifier.fillMaxSize().background(colors.pageBackground)) {
            Column(
                Modifier
                    .fillMaxSize()
                    // 2026-10-09 (design handoff): screen gutter 16 -> 20dp.
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 2026-08-20 (design handoff pass): spec screen 6 header -- title + live result
                // count on the left, a 38x38 cream profile-shortcut circle on the right. Wasn't
                // there before; ExploreScreen had no header of its own at all.
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.explore),
                            style = textStyles.screenTitleTabRoot,
                            color = colors.textPrimary
                        )
                        Text(
                            text = stringResource(Res.string.excursions_count, state.value.items.size),
                            style = textStyles.metadata,
                            color = colors.textSecondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(colors.cream, Radii.pillShape)
                            .bounceClick(scaleDown = 0.92f) { onProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_profile),
                            contentDescription = null,
                            tint = colors.redFill,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                SearchBar(
                    Modifier,
                    isloadingn = state.value.categoryState.isLoadingCategory
                ) { value ->
                    viewModel.onExploreEventChanged(
                        ExploreEvent.OnFilterChanged(value)
                    )
                }

                CategorySection(
                    categoryState = state.value.categoryState,
                    onCategorySelected = { event -> viewModel.onExploreEventChanged(event) },
                )

                PullToRefreshContainer(
                    modifier = Modifier.weight(1f),
                    isRefreshing = state.value.isRefreshing,
                    onRefresh = {
                        viewModel.onExploreEventChanged(ExploreEvent.RefreshContent)
                    }
                ) {
                    if (!state.value.categoryState.isLoadingSkeleton && state.value.items.isEmpty()) {
                        // 2026-08-20 (design handoff pass): spec screen 9, "Explorar -- sin
                        // resultados."
                        // 2026-10-09: "Borrar filtros" now resets ALL filters (category, budget,
                        // dates, text) via OnFilterResetAll -- it used to reset only the category.
                        EmptyState(
                            message = stringResource(Res.string.no_results_title),
                            subtitle = stringResource(Res.string.no_results_subtitle),
                            actionText = stringResource(Res.string.clear),
                            onAction = {
                                viewModel.onExploreEventChanged(ExploreEvent.OnFilterResetAll)
                            }
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (state.value.categoryState.isLoadingSkeleton) {
                                item { TripItemLoadingSection() }
                            } else {
                                items(state.value.items) { item ->
                                    TripItem(
                                        item,
                                        onClick = { goDetails(item) },
                                        toggleFavorite = { event ->
                                            viewModel.onExploreEventChanged(event)
                                        }
                                    )
                                }

                                if (state.value.isLoading && !state.value.categoryState.isLoadingSkeleton) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(32.dp),
                                                color = colors.redFill,
                                                strokeWidth = 2.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (state.value.isLoading && state.value.items.isEmpty() && !state.value.categoryState.isLoadingSkeleton) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colors.redFill,
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp
                    )
                }
            }

            if (state.value.shouldBottomModal) {
                BottomModalSheet(
                    sheetState = bottomState,
                    onDismiss = {
                        viewModel.onExploreEventChanged(
                            ExploreEvent.OnFilterChanged(false)
                        )
                    },
                    content = {
                        HomeFilterContent(
                            exploreState = state.value,
                            onEventChanged = { event -> viewModel.onExploreEventChanged(event) },
                            onOpenCalendar = { showCalendar = true },
                        )
                    },
                )
            }

            if (showCalendar) {
                val tz = TimeZone.currentSystemDefault()
                val filter = state.value.filterState
                Dialog(
                    onDismissRequest = { showCalendar = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                ) {
                    CalendarScreen(
                        initialStart = if (filter.selectedHasExactRange) filter.selectedLeavingTimeStart.toLocalDateTime(tz).date else null,
                        initialEnd = if (filter.selectedHasExactRange) filter.selectedReturningTimeEnd.toLocalDateTime(tz).date else null,
                        onBack = { showCalendar = false },
                        onApply = { start, end ->
                            viewModel.onExploreEventChanged(
                                ExploreEvent.OnFilterSelectedDateRange(leavingDate = start, returningDate = end)
                            )
                            showCalendar = false
                        },
                    )
                }
            }
        }
    }
}
