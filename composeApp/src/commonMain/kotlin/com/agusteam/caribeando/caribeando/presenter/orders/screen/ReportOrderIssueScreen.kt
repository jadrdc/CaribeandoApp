package com.agusteam.caribeando.presenter.orders.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.shopping.viewmodels.ReportOrderViewModel
import com.agusteam.caribeando.presenter.theme.primary
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.report
import caribeando.composeapp.generated.resources.report_order_issues

@Composable
fun ReportOrderIssueScreen(
    viewModel: ReportOrderViewModel = koinViewModel(), orderId: String, onBackPressed: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events
    ObserveAsEvents(event) { event ->
        if (event is ReportOrderViewModel.ReportOrderEvent.OrderReportedSuccefull) {
            onBackPressed()
        }
    }


    LaunchedEffect(Unit) {
        viewModel.handlerEvent(ReportOrderViewModel.ReportOrderEvent.OnOrderLoad(orderId))
    }
    ErrorModal(title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null, onDismiss = {
            viewModel.handlerEvent(ReportOrderViewModel.ReportOrderEvent.ErrorCleared(state.modalType))
        })

    Box {
        LazyColumn(
            Modifier.padding(horizontal = 16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Column(

                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NavigationBar(title = stringResource(Res.string.report_order_issues),
                        onBackPressed = { onBackPressed() })

                    BasicTextField(
                        value = state.message,
                        onValueChange = {
                            viewModel.handlerEvent(
                                ReportOrderViewModel.ReportOrderEvent.OnMessageChanged(
                                    it
                                )
                            )
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth()
                            .height(300.dp) // Altura personalizada para simular un TextArea
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                            .padding(8.dp) // Margen interno
                    )
                }
            }
            item {
                Box(Modifier.padding(vertical = 16.dp)) {
                ActionButton(
                    isValid = state.isValid(),
                    text = stringResource(Res.string.report),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModel.handlerEvent(ReportOrderViewModel.ReportOrderEvent.ReportOrder)
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
                .clickable(enabled = false) {}, // Prevents interaction
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primary)
        }
    }
}