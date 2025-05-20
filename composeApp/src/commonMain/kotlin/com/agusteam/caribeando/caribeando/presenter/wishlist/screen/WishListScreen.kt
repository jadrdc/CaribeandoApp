package com.agusteam.caribeando.presenter.wishlist.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.common.ErrorState
import com.agusteam.caribeando.presenter.common.PullToRefreshContainer
import com.agusteam.caribeando.presenter.wishlist.composable.WishListItemSectionLoading
import com.agusteam.caribeando.presenter.wishlist.composable.WishListSection
import com.agusteam.caribeando.presenter.wishlist.viewmodels.WishListItemViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishListScreen(
    viewModel: WishListItemViewModel = koinViewModel(),
    goDetails: (TripModel) -> Unit
) {


    val state = viewModel.state.collectAsStateWithLifecycle()
    if (state.value.errorState) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ErrorState()
        }
    } else {
        PullToRefreshContainer(
            modifier = Modifier,
            isRefreshing = state.value.isRefreshing,
            onRefresh = {
                viewModel.handleEvent(WishListItemViewModel.WishListEvent.RefreshContent)
            }
        ) {
            if (state.value.isLoading) {
                WishListItemSectionLoading()
            } else {

                WishListSection(state.value.favoriteItems, goDetails)
            }
        }
    }
}