package com.agusteam.caribeando.presenter.orders.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.cancellation_policy
import caribeando.composeapp.generated.resources.destiny
import caribeando.composeapp.generated.resources.ic_pin
import caribeando.composeapp.generated.resources.trip_categories
import com.agusteam.caribeando.presenter.common.CancellationPolicy
import com.agusteam.caribeando.presenter.common.ItemProviderOverviewItem
import com.agusteam.caribeando.presenter.common.MapDetails
import com.agusteam.caribeando.presenter.orders.viewmodels.WishListOrderDetailViewModel
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingItemHeader
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingItemIncluded
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingItemOverview
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingitemContent
import com.agusteam.caribeando.presenter.shopping.model.ShoppingDetailModel
import com.agusteam.caribeando.presenter.wishlist.navigation.WishListItemDetailScreenRoute
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderItemDetailsScreen(
    viewModel: WishListOrderDetailViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    goDetails: (String) -> Unit,
    model: WishListItemDetailScreenRoute
) {
    LaunchedEffect(model.tripId, model.businessId) {
        viewModel.handleEvent(
            WishListOrderDetailViewModel.OrderDetailsEvent.OrderDetailsLoadIncludeServices(
                model.tripId, model.businessName, model.month
            )
        )
    }

    val state = viewModel.state.collectAsStateWithLifecycle()
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier, // Add padding to prevent overlap
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ShoppingItemHeader(
                    images = state.value.galleryPhotos,
                    isSavedForLater = true,
                    onBackPressed = onBackPressed
                )
            }
            item {
                ShoppingItemOverview(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = model.name,
                    description = state.value.description
                )
            }
            item {
                ItemProviderOverviewItem(
                    modifier = Modifier, state =
                    state.value.itemProviderState
                ) {
                    goDetails(model.businessId)
                }
            }
            item {
                ShoppingitemContent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    itemsDetails = listOf(
                        ShoppingDetailModel(
                            title = stringResource(Res.string.destiny),
                            description = model.destiny,
                            icon = Res.drawable.ic_pin
                        ),
                    )
                )
            }
            item {
                MapDetails(lat = model.lat.toDouble(), lng = model.lng.toDouble())
            }
            item {
                CancellationPolicy(
                    title = stringResource(Res.string.cancellation_policy),
                    description = state.value.cancellationPolicy,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {

                }
            }
            item {
                ShoppingItemIncluded(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = stringResource(Res.string.trip_categories),
                    items = state.value.includedServices
                )
            }
        }

    }
}
