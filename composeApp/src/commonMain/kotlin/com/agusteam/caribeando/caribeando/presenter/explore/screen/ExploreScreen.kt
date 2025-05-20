package com.agusteam.caribeando.presenter.explore.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.common.BottomModalSheet
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ErrorState
import com.agusteam.caribeando.presenter.common.PullToRefreshContainer
import com.agusteam.caribeando.presenter.common.SearchBar
import com.agusteam.caribeando.presenter.common.loading.TripItemLoadingSection
import com.agusteam.caribeando.presenter.explore.composable.CategorySection
import com.agusteam.caribeando.presenter.explore.composable.HomeFilterContent
import com.agusteam.caribeando.presenter.explore.composable.TripItem
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreViewModel
import com.agusteam.caribeando.presenter.theme.backGround
import com.agusteam.caribeando.presenter.theme.primary
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = koinViewModel(),
    goDetails: (TripModel) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val bottomState = rememberModalBottomSheetState(true)
    val listState = rememberLazyListState()

    // Enhanced Pagination Trigger
    LaunchedEffect(listState, state.value.items.size, state.value.isLoading) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            lastVisible to total
        }
            .distinctUntilChanged()
            .filter { (lastVisible, total) ->
                val canLoadMore = !state.value.categoryState.isLoadingSkeleton &&
                        !state.value.isLoading &&
                        total > 0 &&
                        lastVisible >= total - 3

                println("Pagination Check: lastVisible=$lastVisible, total=$total, canLoadMore=$canLoadMore")
                canLoadMore
            }
            .collectLatest {
                println("Triggering LoadMoreTrips")
                viewModel.onExploreEventChanged(ExploreEvent.LoadMoreTrips)
            }
    }

    // Error Modal
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
            ErrorState()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize().background(backGround)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (state.value.categoryState.isLoadingSkeleton) {
                            item {
                                TripItemLoadingSection()
                            }
                        } else {
                            items(
                                items = state.value.items,
                                // key = { it.id }
                            ) { item ->
                                TripItem(
                                    item,
                                    onClick = { goDetails(item) },
                                    toggleFavorite = { event ->
                                        viewModel.onExploreEventChanged(event)
                                    }
                                )
                            }

                            // Show loading indicator only during pagination
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
                                            color = primary,
                                            strokeWidth = 2.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Show loading overlay only for initial load
            if (state.value.isLoading && state.value.items.isEmpty() && !state.value.categoryState.isLoadingSkeleton) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = primary,
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp
                    )
                }
            }

            // Filter Bottom Sheet
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
                        )
                    },
                )
            }
        }
    }
}