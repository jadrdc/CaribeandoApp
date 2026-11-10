package com.agusteam.caribeando.presenter.orders.screen

import caribeando.composeapp.generated.resources.report_placeholder
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.shopping.viewmodels.ReportOrderViewModel
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.order_label
import caribeando.composeapp.generated.resources.report
import caribeando.composeapp.generated.resources.report_helper_text
import caribeando.composeapp.generated.resources.report_issue
import caribeando.composeapp.generated.resources.report_issue_title

/**
 * The handoff's "22. Reportar inconveniente". Restyled per spec: NavigationBar (back+title, the
 * same shared header used across every pushed screen in this app, kept for consistency rather
 * than a one-off size), a "Cuéntanos tu problema" headline, an "Orden <código> · <viaje>"
 * subtitle, a taller bordered textarea with a helper line, sticky submit.
 *
 * 2026-08-20 (design handoff pass): [tripTitle] is a new, purely-cosmetic param for the subtitle
 * -- `ReportOrderState`/`ReportOrderViewModel` are untouched, since the trip name is only needed
 * for display and the caller (`ShoppingOrderDetailScreen`) already has it in scope.
 *
 * The success confirmation intentionally stays the existing `ErrorModal` flow (real backend copy:
 * "Se ha reportado un inconveniente...") rather than swapping in the spec's generic
 * "Reporte enviado. Te contactamos pronto." toast -- that would trade a real, backend-driven
 * message for a static one.
 *
 * 2026-08-20 (re-verification pass): the `NavigationBar` header was reusing
 * `Res.string.report_order_issues` ("Reportar inconveniente con la orden" -- a DIFFERENT
 * verbatim string, the order-detail screen's TextLink label per the spec) instead of the
 * header's own verbatim "Reportar inconveniente". Added a dedicated `report_issue_title` string
 * resource rather than truncating/reusing the wrong one.
 */
@Composable
fun ReportOrderIssueScreen(
    viewModel: ReportOrderViewModel = koinViewModel(),
    orderId: String,
    tripTitle: String = "",
    onBackPressed: () -> Unit
) {
    val colors = caribeandoColors
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
            Modifier.padding(horizontal = 20.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NavigationBar(title = stringResource(Res.string.report_issue_title),
                        onBackPressed = { onBackPressed() })

                    Column {
                        Text(
                            text = stringResource(Res.string.report_issue),
                            color = colors.textPrimary,
                            fontSize = 21.sp,
                            lineHeight = 27.3.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        val subtitle = buildString {
                            append(stringResource(Res.string.order_label))
                            append(" ")
                            append(orderId)
                            if (tripTitle.isNotBlank()) {
                                append(" · ")
                                append(tripTitle)
                            }
                        }
                        Text(
                            modifier = Modifier.padding(top = 6.dp),
                            text = subtitle,
                            color = colors.textSecondary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Column {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .height(240.dp)
                                .border(1.dp, colors.panelFieldBorder, Radii.panelShape)
                        ) {
                            if (state.message.isEmpty()) {
                                // 2026-08-20 (screens 18-30 fidelity pass): was a hardcoded,
                                // differently-worded placeholder -- spec's verbatim text.
                                Text(
                                    modifier = Modifier.padding(14.dp),
                                    text = stringResource(Res.string.report_placeholder),
                                    color = colors.textSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 20.8.sp,
                                )
                            }
                            BasicTextField(
                                value = state.message,
                                onValueChange = {
                                    viewModel.handlerEvent(
                                        ReportOrderViewModel.ReportOrderEvent.OnMessageChanged(
                                            it
                                        )
                                    )
                                },
                                textStyle = TextStyle(fontSize = 13.sp, lineHeight = 20.8.sp, color = colors.textPrimary),
                                modifier = Modifier.fillMaxWidth().fillMaxSize()
                                    .padding(14.dp)
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = stringResource(Res.string.report_helper_text),
                            color = colors.textSecondary,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
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
            CircularProgressIndicator(color = colors.redFill)
        }
    }
}
