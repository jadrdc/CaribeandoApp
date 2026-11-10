package com.agusteam.caribeando.caribeando.presenter.orders.screen

import caribeando.composeapp.generated.resources.star_description
import caribeando.composeapp.generated.resources.how_was_trip
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.common.Toast
import com.agusteam.caribeando.presenter.shopping.viewmodels.RatingOrderViewModel
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.rate_choose_star_toast
import caribeando.composeapp.generated.resources.rate_trip_title
import caribeando.composeapp.generated.resources.rating_caption_0
import caribeando.composeapp.generated.resources.rating_caption_1
import caribeando.composeapp.generated.resources.rating_caption_2
import caribeando.composeapp.generated.resources.rating_caption_3
import caribeando.composeapp.generated.resources.rating_caption_4
import caribeando.composeapp.generated.resources.rating_caption_5
import caribeando.composeapp.generated.resources.review_placeholder
import caribeando.composeapp.generated.resources.submit_review
import caribeando.composeapp.generated.resources.tell_us_more

/**
 * The handoff's "24. Calificar viaje". Restyled per spec: a 64x64 thumb + title/meta header,
 * a 5-star row at 42dp (10dp apart) with a caption that tracks the selected score, a
 * "Cuéntanos más" textarea (118dp tall, `colors.fieldFill`), and validation that blocks submit
 * with 0 stars via the shared pinned `Toast` ("Elige una calificación primero") instead of
 * silently disabling the button.
 *
 * 2026-08-20 (design handoff pass): [tripTitle]/[tripImage]/[date]/[providerName] are new,
 * purely-cosmetic params for the header -- `RatingOrderState`/`RatingOrderViewModel` are
 * untouched; both call sites (Viajes pasados list, order-detail screen) already have this data
 * and pass it through `RatingTripRoute` rather than it being re-fetched or faked.
 *
 * Real behavior change, flagged: the old `isValid()` gate (`rating >= 0 && comment.isNotBlank()`)
 * disabled the submit button entirely whenever the comment was blank -- with 0 stars AND a blank
 * comment (the screen's actual starting state), that silently blocked ANY tap, so the spec's
 * "submitting with no stars shows a toast" could never fire. `ActionButton` is no longer gated on
 * `state.isValid()` here; it's always tappable, and this screen's own onClick decides: 0 stars ->
 * toast, else -> submit (comment may now be blank -- the backend's handling of a blank comment on
 * `CommentOrder` hasn't been exercised before and isn't verified here).
 */
@Composable
fun ReviewOrderScreen(
    viewModel: RatingOrderViewModel = koinViewModel(),
    orderId: String,
    tripTitle: String = "",
    tripImage: String = "",
    date: String = "",
    providerName: String = "",
    onAction: (Boolean) -> Unit
) {
    val colors = caribeandoColors
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events
    var toastMessage by remember { mutableStateOf<String?>(null) }
    ObserveAsEvents(event) { event ->
        if (event is RatingOrderViewModel.RatingOrderEvents.RatedOrder) {
            onAction(true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handlerEvent(
            RatingOrderViewModel.RatingOrderEvents.OnOrderLoad(
                orderId
            )
        )
    }

    ErrorModal(
        title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null,
        onDismiss = {
            viewModel.handlerEvent(
                RatingOrderViewModel.RatingOrderEvents.ErrorCleared(
                    state.modalType
                )
            )
        }
    )

    val chooseStarToast = stringResource(Res.string.rate_choose_star_toast)

    Box {
        LazyColumn(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 2026-08-20 (screens 18-30 fidelity pass): was hardcoded "Reseña del
                    // pedido" -- spec screen 24's title is verbatim "Calificar viaje".
                    NavigationBar(
                        title = stringResource(Res.string.rate_trip_title),
                        onBackPressed = { onAction(false) }
                    )

                    if (tripTitle.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier.size(64.dp).clip(Radii.cardShape),
                                model = tripImage,
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                            Column {
                                Text(
                                    text = tripTitle,
                                    color = colors.textPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                val meta = if (providerName.isNotBlank()) "$date · $providerName" else date
                                if (meta.isNotBlank())
                                    Text(
                                        modifier = Modifier.padding(top = 2.dp),
                                        text = meta,
                                        color = colors.textSecondary,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                            }
                        }
                    }

                    Text(
                        text = stringResource(Res.string.how_was_trip),
                        color = colors.textPrimary,
                        fontSize = 17.sp,
                        lineHeight = 22.95.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        StarSelector(
                            rating = state.rating,
                            onRatingSelected = {
                                viewModel.handlerEvent(
                                    RatingOrderViewModel.RatingOrderEvents.OnRatingChanged(it)
                                )
                            }
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = ratingCaption(state.rating),
                            color = colors.textSecondary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(Res.string.tell_us_more),
                            color = colors.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(118.dp)
                                .background(colors.fieldFill, Radii.panelShape)
                        ) {
                            BasicTextField(
                                value = state.comment,
                                onValueChange = {
                                    viewModel.handlerEvent(
                                        RatingOrderViewModel.RatingOrderEvents.OnCommentChanged(it)
                                    )
                                },
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, lineHeight = 19.5.sp, color = colors.textPrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxSize()
                                    .padding(14.dp),
                                decorationBox = { innerTextField ->
                                    if (state.comment.isEmpty()) {
                                        Text(
                                            text = stringResource(Res.string.review_placeholder),
                                            color = colors.textSecondary,
                                            fontSize = 13.sp,
                                            lineHeight = 19.5.sp,
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }
                }
            }
            item {
                Box(Modifier.padding(vertical = 16.dp)) {
                    ActionButton(
                        text = stringResource(Res.string.submit_review),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.rating <= 0) {
                            toastMessage = chooseStarToast
                        } else {
                            viewModel.handlerEvent(RatingOrderViewModel.RatingOrderEvents.CommentOrder)
                        }
                    }
                }
            }
        }
        Toast(message = toastMessage, onDismissed = { toastMessage = null })
    }
    // Loading overlay
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = colors.redFill)
        }
    }
}

@Composable
private fun ratingCaption(rating: Int): String {
    return when (rating) {
        1 -> stringResource(Res.string.rating_caption_1)
        2 -> stringResource(Res.string.rating_caption_2)
        3 -> stringResource(Res.string.rating_caption_3)
        4 -> stringResource(Res.string.rating_caption_4)
        5 -> stringResource(Res.string.rating_caption_5)
        else -> stringResource(Res.string.rating_caption_0)
    }
}

@Composable
fun StarSelector(
    rating: Int,
    onRatingSelected: (Int) -> Unit,
    maxStars: Int = 5
) {
    val colors = caribeandoColors
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = stringResource(Res.string.star_description, i),
                tint = if (i <= rating) colors.star else colors.emptyStar,
                modifier = Modifier
                    .size(42.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onRatingSelected(i) }
                    )
            )
        }
    }
}
