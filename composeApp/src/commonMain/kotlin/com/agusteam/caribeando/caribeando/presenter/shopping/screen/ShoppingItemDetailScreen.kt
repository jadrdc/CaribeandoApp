package com.agusteam.caribeando.presenter.shopping.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.arrival_time
import caribeando.composeapp.generated.resources.cancellation_policy
import caribeando.composeapp.generated.resources.destiny
import caribeando.composeapp.generated.resources.ic_address
import caribeando.composeapp.generated.resources.ic_cash
import caribeando.composeapp.generated.resources.ic_clock
import caribeando.composeapp.generated.resources.ic_pin
import caribeando.composeapp.generated.resources.included_services
import caribeando.composeapp.generated.resources.initial_payment
import caribeando.composeapp.generated.resources.leaving_time
import caribeando.composeapp.generated.resources.pay
import caribeando.composeapp.generated.resources.starting_place
import caribeando.composeapp.generated.resources.total_payment
import com.agusteam.caribeando.caribeando.presenter.common.CommentsSection
import com.agusteam.caribeando.domain.models.PayItemModel
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.AnimationLoading
import com.agusteam.caribeando.presenter.common.CancellationPolicy
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ItemProviderOverviewItem
import com.agusteam.caribeando.presenter.common.MapDetails
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.home.navigation.TripDetailScreenRoute
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingItemHeader
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingItemIncluded
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingItemOverview
import com.agusteam.caribeando.presenter.shopping.composable.ShoppingitemContent
import com.agusteam.caribeando.presenter.shopping.model.ShoppingDetailModel
import com.agusteam.caribeando.presenter.shopping.viewmodels.ShoppingItemDetailsViewModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShoppingItemDetailScreen(
    viewModel: ShoppingItemDetailsViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    goProviderProfile: (id: String) -> Unit,
    goGallery: (images: List<String>, startIndex: Int) -> Unit = { _, _ -> },
    payItem: (
        PayItemModel
    ) -> Unit,
    model: TripDetailScreenRoute,
) {
    LaunchedEffect(model.tripId) {
        viewModel.loadShoppingDetails(model)
    }

    val state = viewModel.state.collectAsStateWithLifecycle().value
    ErrorModal(title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null, onDismiss = {
            viewModel.handleEvent(ShoppingItemDetailsViewModel.ShoppingDetailEvent.OnErrorModalAccepted)
        })


    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.padding(
                bottom = 96.dp,
            ), // Clears the sticky payment bar
            horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
            ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.tripId.isNotBlank()) {
                item {
                    ShoppingItemHeader(
                        images = state.galleryPhotos,
                        isSavedForLater = state.isMarkedAsFavorite,
                        onBackPressed = onBackPressed,
                        onOpenGallery = { index -> goGallery(state.galleryPhotos, index) }
                    ) {
                        viewModel.handleEvent(ShoppingItemDetailsViewModel.ShoppingDetailEvent.MarkFavorite)
                    }
                }
                item {
                    ShoppingItemOverview(
                        reviewCount = model.reviewCount,
                        rating = model.rating.toDouble(),
                        modifier = Modifier.padding(horizontal = 20.dp),
                        title = state.title,
                        description = state.description
                    )
                }
            }
            if (state.isLoadingContent) {
                item {
                    Box(Modifier.align(CenterEnd).padding(top = 16.dp)) {
                        AnimationLoading()
                    }
                }
            } else if (state.tripId.isNotBlank()) {

                item {
                    ItemProviderOverviewItem(
                        modifier = Modifier, state =
                        state.itemProviderState
                    ) {
                        goProviderProfile(state.businessId)
                    }
                }
                item {
                    // 2026-08-20 (screens 1-17 fidelity pass): reordered to the handoff's exact
                    // "Destino, Lugar de salida, Hora de salida, Hora de llegada, Pago de
                    // reserva, Pago total" -- this component's own doc comment already claimed
                    // that order, but the list built here was actually Destino/Pago de
                    // reserva/Pago total/Lugar de salida/Hora de salida/Hora de llegada, which
                    // paired the wrong items together in the real 2-column grid.
                    ShoppingitemContent(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        itemsDetails = listOf(
                            ShoppingDetailModel(
                                title = stringResource(Res.string.destiny),
                                description = state.destiny,
                                icon = Res.drawable.ic_pin
                            ),
                            ShoppingDetailModel(
                                title = stringResource(Res.string.starting_place),
                                description = state.meetingPoint,
                                icon = Res.drawable.ic_address
                            ),
                            ShoppingDetailModel(
                                title = stringResource(Res.string.leaving_time),
                                description = state.leavingTime,
                                icon = Res.drawable.ic_clock
                            ),
                            ShoppingDetailModel(
                                title = stringResource(Res.string.arrival_time),
                                description = state.arrivingTime,
                                icon = Res.drawable.ic_clock
                            ),
                            ShoppingDetailModel(
                                title = stringResource(Res.string.initial_payment),
                                description = formatMoney(state.initialPayment),
                                icon = Res.drawable.ic_cash
                            ),
                            ShoppingDetailModel(
                                title = stringResource(Res.string.total_payment),
                                description = formatMoney(state.totalPayment),
                                icon = Res.drawable.ic_cash
                            )
                        )
                    )
                }
                item {
                    MapDetails(lat = state.lat, lng = state.lng)
                }
                item {
                    CancellationPolicy(
                        title = stringResource(Res.string.cancellation_policy),
                        description = state.cancellationPolicy,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {

                    }
                }
                item {
                    ShoppingItemIncluded(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        title = stringResource(Res.string.included_services),
                        items = state.includedServices
                    )
                }
                item {
                    if (state.comments.isNotEmpty()) {
                        Column {
                            CommentsSection(state.comments, reviewCount = model.reviewCount)
                        }
                    }
                }

            }
        }


        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {}, // Prevents interaction
                contentAlignment = Alignment.Center
            ) {
                // 2026-08-20 (dark-mode audit): cosmetic-only -- `primary` and `colors.redFill`
                // are identical in both themes, so this was never a visible bug, just
                // inconsistent with sibling screens (Login/SignUp/ForgotPassword/Review/Report)
                // which all use `colors.redFill` for the same spinner.
                CircularProgressIndicator(color = caribeandoColors.redFill)
            }
        }

        if (state.tripId.isNotBlank()) {
            val colors = caribeandoColors
            Column(Modifier.align(Alignment.BottomCenter).background(colors.stickyBar)) {
                HorizontalDivider(color = colors.hairline)
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = formatMoney(state.totalPayment),
                            color = colors.textPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "por persona · reserva ${formatMoney(state.initialPayment)}",
                            color = colors.textSecondary,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    ActionButton(
                        text = stringResource(Res.string.pay),
                        modifier = Modifier.widthIn(max = 158.dp)
                    ) {
                        payItem(
                            PayItemModel(
                                title = state.title,
                                destiny = state.destiny,
                                profilePhoto = state.galleryPhotos.firstOrNull() ?: "",
                                leavingTime = state.leavingTime,
                                meetingPoint = state.meetingPoint,
                                initialPayment = state.initialPayment,
                                totalPayment = state.totalPayment,
                                tripSchedule = model.tripScheduleId,
                                arrivingTime = state.arrivingTime,
                                businessName = state.itemProviderState.businessName,
                                businessMonth = state.itemProviderState.month.toString(),
                                businessPhoto = state.itemProviderState.businessImage,
                                galleryPhoto = state.galleryPhotos
                            )
                        )
                    }
                }
            }
        }
    }
}
