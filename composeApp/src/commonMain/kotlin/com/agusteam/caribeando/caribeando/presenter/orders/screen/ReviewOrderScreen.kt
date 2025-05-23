package com.agusteam.caribeando.caribeando.presenter.orders.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.shopping.viewmodels.RatingOrderViewModel
import com.agusteam.caribeando.presenter.theme.primary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReviewOrderScreen(
    viewModel: RatingOrderViewModel = koinViewModel(),
    orderId: String,
    onAction: (Boolean) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events
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

    Box {
        LazyColumn(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NavigationBar(
                        title = "Reseña del pedido",
                        onBackPressed = { onAction(false) }
                    )

                    // Star Selector
                    StarSelector(
                        rating = state.rating,
                        onRatingSelected = {
                            viewModel.handlerEvent(
                                RatingOrderViewModel.RatingOrderEvents.OnRatingChanged(it)
                            )
                        }
                    )

                    Text(
                        text = "Tu reseña",
                        fontSize = 15.sp,
                        color = Color(0xFF555555),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    BasicTextField(
                        value = state.comment,
                        onValueChange = {
                            viewModel.handlerEvent(
                                RatingOrderViewModel.RatingOrderEvents.OnCommentChanged(it)
                            )
                        },
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                            .padding(8.dp),
                        decorationBox = { innerTextField ->
                            if (state.comment.isEmpty()) {
                                Text(
                                    text = "Escribe tus comentarios aquí...",
                                    color = Color(0xFFAAAAAA),
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
            item {
                Box(Modifier.padding(vertical = 16.dp)) {
                    ActionButton(
                        isValid = state.isValid(),
                        text = "Enviar reseña",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.handlerEvent(RatingOrderViewModel.RatingOrderEvents.CommentOrder)
                    }
                }
            }
        }
    }
    // Loading overlay
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primary)
        }
    }
}

@Composable
fun StarSelector(
    rating: Int,
    onRatingSelected: (Int) -> Unit,
    maxStars: Int = 5
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Estrella $i",
                tint = if (i <= rating) Color(0xFFFFC107) else Color(0xFFBDBDBD),
                modifier = Modifier
                    .size(36.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,  // This removes the ripple effect
                        onClick = { onRatingSelected(i) }
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}