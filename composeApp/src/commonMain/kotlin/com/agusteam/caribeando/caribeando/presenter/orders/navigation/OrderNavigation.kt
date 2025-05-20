package com.agusteam.caribeando.presenter.orders.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class OrderHistoryNavigation(val route: String) {
    data object OrderHistoryScreen : OrderHistoryNavigation("order_history")

    @Serializable
    data class ReportOrderIssuesScreen(val orderId: String) : OrderHistoryNavigation("report_order")
}