package com.agusteam.caribeando.presenter.shopping.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.agusteam.caribeando.presenter.home.navigation.TripDetailScreenRoute
import com.agusteam.caribeando.presenter.orders.navigation.OrderHistoryNavigation
import com.agusteam.caribeando.presenter.orders.screen.ReportOrderIssueScreen
import com.agusteam.caribeando.presenter.profile.screen.TripProviderProfileScreen
import com.agusteam.caribeando.presenter.shopping.screen.ShoppingItemDetailScreen
import com.agusteam.caribeando.presenter.shopping.screen.ShoppingOrderDetailScreen
import com.agusteam.caribeando.presenter.shopping.screen.TripItemPayingScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun ShoppingFlowNavigation(
    onBackPressed: () -> Unit,
    modelRoute: TripDetailScreenRoute,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ShoppingNavigationRoutes.ShoppingItemDetailScreen.route
    ) {
        composable(ShoppingNavigationRoutes.ShoppingItemDetailScreen.route) {
            ShoppingItemDetailScreen(onBackPressed = { onBackPressed() }, payItem = { payItem ->
                navController.navigate(
                    ShoppingItemPayingScreen(
                        title = payItem.title,
                        destiny = payItem.destiny,
                        profilePhoto = payItem.profilePhoto,
                        leavingTime = payItem.leavingTime,
                        meetingPoint = payItem.meetingPoint,
                        initialPayment = payItem.initialPayment,
                        totalPayment = payItem.totalPayment,
                        tripDetailId = payItem.tripSchedule,
                        arrivingTime = payItem.arrivingTime,
                        businessPhoto = payItem.businessPhoto,
                        businessName = payItem.businessName,
                        businessMonth = payItem.businessMonth,
                        galleryPhotoJson = Json.encodeToString(payItem.galleryPhoto), // 👈 serializa aquí

                    )
                )
            }, goProviderProfile = { id ->
                navController.navigate(ProviderProfileScreen(id))
            }, model = modelRoute)
        }
        composable<ProviderProfileScreen> { backStackEntry ->
            val tripProviderModel = backStackEntry.toRoute<ProviderProfileScreen>()
            TripProviderProfileScreen(businessId = tripProviderModel.businessId, onBackPressed = {
                navController.navigate(ShoppingNavigationRoutes.ShoppingItemDetailScreen.route)
            })
        }
        composable<ShoppingItemPayingScreen> { backStackEntry ->
            val model = backStackEntry.toRoute<ShoppingItemPayingScreen>()
            TripItemPayingScreen(
                model = model,
                onBackPressed = {
                    navController.navigate(ShoppingNavigationRoutes.ShoppingItemDetailScreen.route)
                }, onPaymentSuccessFull = { order ->
                    navController.navigate(
                        ShoppingOrderDetailScreenRoute(
                            scheduledId = "",
                            tripTitle = order.tripTitle,
                            amount = order.amount.toInt(),
                            transactionId = order.transactionId,
                            dateFrom = order.dateFrom,
                            dateTo = order.dateTo,
                            businessMonth = order.businessMonth,
                            businessName = order.businessName,
                            businessPhoto = order.businessPhoto,
                            galleryPhoto = order.galleryPhotos
                        )
                    )
                })
        }
        composable<ShoppingOrderDetailScreenRoute> { backStackEntry ->
            val model = backStackEntry.toRoute<ShoppingOrderDetailScreenRoute>()
            ShoppingOrderDetailScreen(
                model = model,
                rateOrder = {

                },
                onBackPressed = { onBackPressed() },
                reportOrder = {
                    navController.navigate(OrderHistoryNavigation.ReportOrderIssuesScreen(orderId = model.transactionId))
                })
        }
        composable<OrderHistoryNavigation.ReportOrderIssuesScreen> { backStackEntry ->
            val model = backStackEntry.toRoute<OrderHistoryNavigation.ReportOrderIssuesScreen>()

            ReportOrderIssueScreen(
                orderId = model.orderId,
                onBackPressed = {
                    navController.popBackStack()
                })
        }
    }
}