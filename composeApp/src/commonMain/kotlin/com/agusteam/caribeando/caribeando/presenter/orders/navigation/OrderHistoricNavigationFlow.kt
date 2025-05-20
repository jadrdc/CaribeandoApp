package com.agusteam.caribeando.presenter.orders.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.agusteam.caribeando.presenter.orders.screen.OrderHistoryScreen
import com.agusteam.caribeando.presenter.orders.screen.ReportOrderIssueScreen
import com.agusteam.caribeando.presenter.shopping.navigation.ShoppingOrderDetailScreenRoute
import com.agusteam.caribeando.presenter.shopping.screen.ShoppingOrderDetailScreen

@Composable
fun OrderHistoryNavigationFlow(
    showBottomNav: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    var orderRoute: ShoppingOrderDetailScreenRoute? = remember { null }

    NavHost(
        navController = navController,
        startDestination = OrderHistoryNavigation.OrderHistoryScreen.route
    ) {
        composable(OrderHistoryNavigation.OrderHistoryScreen.route) {
            OrderHistoryScreen { order ->
                showBottomNav(false)
                orderRoute = ShoppingOrderDetailScreenRoute(
                    tripTitle = order.tripName,
                    amount = order.totalPayment.toInt(),
                    transactionId = order.transactionId,
                    dateFrom = order.dateFrom,
                    dateTo = order.dateTo,
                    businessMonth = order.providerMonth.toString(),
                    businessName = order.providerName,
                    businessPhoto = order.providerImage,
                    galleryPhoto = order.tripImages
                )
                orderRoute?.let {
                    navController.navigate(
                        it
                    )
                }
            }
        }
        composable<ShoppingOrderDetailScreenRoute> { backStackEntry ->
            val model = backStackEntry.toRoute<ShoppingOrderDetailScreenRoute>()
            ShoppingOrderDetailScreen(model = model, onBackPressed = {
                showBottomNav(true)
                navController.navigate(OrderHistoryNavigation.OrderHistoryScreen.route)
            }, reportOrder = {
                navController.navigate(OrderHistoryNavigation.ReportOrderIssuesScreen(orderId = model.transactionId))
            })
        }
        composable<OrderHistoryNavigation.ReportOrderIssuesScreen> { backStackEntry ->
            val model = backStackEntry.toRoute<OrderHistoryNavigation.ReportOrderIssuesScreen>()

            ReportOrderIssueScreen(
                orderId = model.orderId,
                onBackPressed = {
                    orderRoute?.let {
                        navController.navigate(it)
                    }
                })
        }
    }
}