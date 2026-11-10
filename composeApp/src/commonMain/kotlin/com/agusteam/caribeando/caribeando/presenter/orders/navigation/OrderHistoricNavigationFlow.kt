package com.agusteam.caribeando.presenter.orders.navigation

import com.agusteam.caribeando.presenter.home.navigation.CaribeandoTransitions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.agusteam.caribeando.caribeando.presenter.orders.screen.ReviewOrderScreen
import com.agusteam.caribeando.presenter.orders.screen.OrderHistoryScreen
import com.agusteam.caribeando.presenter.orders.screen.ReportOrderIssueScreen
import com.agusteam.caribeando.presenter.shopping.navigation.PayBalanceScreenRoute
import com.agusteam.caribeando.presenter.shopping.navigation.RatingTripRoute
import com.agusteam.caribeando.presenter.shopping.navigation.ShoppingOrderDetailScreenRoute
import com.agusteam.caribeando.presenter.shopping.screen.PayBalanceScreen
import com.agusteam.caribeando.presenter.shopping.screen.ShoppingOrderDetailScreen

@Composable
fun OrderHistoryNavigationFlow(
    showBottomNav: (Boolean) -> Unit,
    // 2026-08-20 (design handoff pass): lets the "25. Viajes — vacío" empty state's "Explorar
    // excursiones" action jump back to the Explorar tab -- this nav graph is nested inside
    // HomeScreen's own NavHost and has no reference to its navController, so the switch has to be
    // handed in from there instead.
    onExploreClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    var orderRoute: ShoppingOrderDetailScreenRoute? = remember { null }

    // 2026-08-20 (design handoff pass): one-shot "a review was just submitted from the list"
    // signal for screen 18/23's toast ("Gracias por tu reseña") + row refresh, mirroring the
    // existing `orderRoute` var above -- both are plain vars captured by this composable and read
    // by the composable() blocks below, not proper nav-result plumbing, but that's the pattern
    // this file already established for passing data back across a pop.
    var justRated by remember { mutableStateOf(false) }

    // 2026-08-20 (re-verification pass): same one-shot-var pattern as [justRated] above, for
    // screen 21's "cancel succeeded -> pop back to the list with a toast" flow (see
    // `ShoppingOrderDetailScreen`'s `onCancelSucceeded` doc comment).
    var cancelledMessage: String? by remember { mutableStateOf(null) }


    NavHost(
        navController = navController,
        startDestination = OrderHistoryNavigation.OrderHistoryScreen.route,
        // 2026-10-09 (design handoff, "Transitions"): push/pop = 300ms fade + slide.
        enterTransition = { CaribeandoTransitions.enter },
        exitTransition = { CaribeandoTransitions.exit },
        popEnterTransition = { CaribeandoTransitions.popEnter },
        popExitTransition = { CaribeandoTransitions.popExit },
    ) {
        composable<RatingTripRoute> { backStackEntry ->
            val model = backStackEntry.toRoute<RatingTripRoute>()
            ReviewOrderScreen(
                orderId = model.orderId,
                tripTitle = model.tripTitle,
                tripImage = model.tripImage,
                date = model.date,
                providerName = model.providerName,
            ) {
                if (model.isFromList) {
                    justRated = true
                    navController.popBackStack()
                } else {
                    orderRoute?.hasItBeenEvaluated = it
                    orderRoute?.let {
                        navController.navigate(it)
                    }
                }
            }
        }
        composable(OrderHistoryNavigation.OrderHistoryScreen.route) {
            OrderHistoryScreen(
                onExploreClick = onExploreClick,
                showRatedToast = justRated,
                onRatedToastShown = { justRated = false },
                cancelledToastMessage = cancelledMessage,
                onCancelledToastShown = { cancelledMessage = null },
                rateOrderTrip = { order ->
                    navController.navigate(
                        RatingTripRoute(
                            orderId = order.transactionId,
                            isFromList = true,
                            tripTitle = order.tripName,
                            tripImage = order.tripImage,
                            date = order.date,
                            providerName = order.providerName,
                        )
                    )
                },
                goDetails = { order, hasItBeenEvaluted, isUpcoming ->
                    showBottomNav(false)
                    orderRoute = ShoppingOrderDetailScreenRoute(
                        scheduledId = order.scheduledId,
                        tripTitle = order.tripName,
                        amount = order.totalPayment.toInt(),
                        transactionId = order.transactionId,
                        orderId = order.orderId,
                        dateFrom = order.dateFrom,
                        dateTo = order.dateTo,
                        businessMonth = order.providerMonth.toString(),
                        businessName = order.providerName,
                        businessPhoto = order.providerImage,
                        galleryPhoto = order.tripImage,
                        hasItBeenEvaluated = hasItBeenEvaluted,
                        isUpcoming = isUpcoming,
                        paidAmount = order.paidAmount.toInt(),
                    )
                    navController.navigate(orderRoute)
                })
        }
        composable<ShoppingOrderDetailScreenRoute> { backStackEntry ->
            val model = backStackEntry.toRoute<ShoppingOrderDetailScreenRoute>()
            ShoppingOrderDetailScreen(model = model,
                hasItBeenEvaluted = model.hasItBeenEvaluated,
                onBackPressed = {
                    showBottomNav(true)
                    navController.navigate(OrderHistoryNavigation.OrderHistoryScreen.route)
                },
                onCancelSucceeded = { message ->
                    cancelledMessage = message
                    showBottomNav(true)
                    navController.navigate(OrderHistoryNavigation.OrderHistoryScreen.route)
                }, reportOrder = {
                    navController.navigate(
                        OrderHistoryNavigation.ReportOrderIssuesScreen(
                            orderId = model.transactionId,
                            tripTitle = model.tripTitle,
                        )
                    )
                }, rateOrder = { orderId, tripTitle, tripImage, date, providerName ->
                    navController.navigate(
                        RatingTripRoute(
                            orderId = orderId,
                            isFromList = false,
                            tripTitle = tripTitle,
                            tripImage = tripImage,
                            date = date,
                            providerName = providerName,
                        )
                    )
                }, payBalance = {
                    // 2026-08-20 (design handoff pass): screen 20, "Pagar faltante" -- orderId
                    // (not transactionId) is what the backend's pay-balance endpoint keys on.
                    navController.navigate(
                        PayBalanceScreenRoute(
                            orderId = model.orderId,
                            transactionId = model.transactionId,
                            tripTitle = model.tripTitle,
                            businessName = model.businessName,
                            businessPhoto = model.businessPhoto,
                            totalPayment = model.amount,
                            paidAmount = model.paidAmount,
                        )
                    )
                }
            )
        }
        composable<PayBalanceScreenRoute> { backStackEntry ->
            val model = backStackEntry.toRoute<PayBalanceScreenRoute>()
            PayBalanceScreen(
                model = model,
                onBackPressed = { navController.popBackStack() },
                onPaymentSuccessFull = {
                    // The order is now fully paid -- update the already-loaded detail route in
                    // place (same pattern as hasItBeenEvaluated above `RatingTripRoute`) so
                    // popping back doesn't show a stale "Falta por pagar" panel, and pop back to
                    // it rather than all the way to the list.
                    orderRoute?.paidAmount = model.totalPayment
                    navController.popBackStack()
                }
            )
        }
        composable<OrderHistoryNavigation.ReportOrderIssuesScreen> { backStackEntry ->
            val model = backStackEntry.toRoute<OrderHistoryNavigation.ReportOrderIssuesScreen>()

            ReportOrderIssueScreen(
                orderId = model.orderId,
                tripTitle = model.tripTitle,
                onBackPressed = {
                    orderRoute?.let {
                        navController.navigate(it)
                    }
                })
        }
    }
}
