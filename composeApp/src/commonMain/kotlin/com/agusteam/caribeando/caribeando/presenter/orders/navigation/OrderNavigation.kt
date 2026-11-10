package com.agusteam.caribeando.presenter.orders.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class OrderHistoryNavigation(val route: String) {
    data object OrderHistoryScreen : OrderHistoryNavigation("order_history")

    @Serializable
    // 2026-08-20 (design handoff pass): [tripTitle] added so screen 22's "Orden <código> ·
    // <viaje>" subtitle can show the real trip name -- the caller (ShoppingOrderDetailScreen)
    // already has it in scope, so it's threaded through the route rather than re-fetched.
    data class ReportOrderIssuesScreen(val orderId: String, val tripTitle: String = "") :
        OrderHistoryNavigation("report_order")
}
