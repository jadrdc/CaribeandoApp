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
import com.agusteam.caribeando.presenter.shopping.screen.GalleryScreen
import com.agusteam.caribeando.presenter.shopping.screen.PaymentSuccessScreen
import com.agusteam.caribeando.presenter.shopping.screen.PaymentFailedScreen
import com.agusteam.caribeando.presenter.shopping.screen.ShoppingOrderDetailScreen
import com.agusteam.caribeando.presenter.shopping.screen.TripItemPayingScreen


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
            ShoppingItemDetailScreen(onBackPressed = { onBackPressed() }, goGallery = { images, startIndex ->
                navController.navigate(
                    GalleryScreenRoute(
                        images = images,
                        startIndex = startIndex,
                        destiny = modelRoute.destiny,
                        businessName = modelRoute.businessName,
                    )
                )
            }, payItem = { payItem ->
                navController.navigate(
                    ShoppingItemPayingScreen(
                        tripId = modelRoute.tripId,
                        title = payItem.title,
                        destiny = payItem.destiny,
                        leavingTime = payItem.leavingTime,
                        meetingPoint = payItem.meetingPoint,
                        initialPayment = payItem.initialPayment,
                        totalPayment = payItem.totalPayment,
                        tripDetailId = payItem.tripSchedule,
                        arrivingTime = payItem.arrivingTime,
                        businessName = payItem.businessName,
                        businessMonth = payItem.businessMonth,
                    )
                )
            }, goProviderProfile = { id ->
                navController.navigate(ProviderProfileScreen(id))
            }, model = modelRoute)
        }
        composable<GalleryScreenRoute> { backStackEntry ->
            val galleryModel = backStackEntry.toRoute<GalleryScreenRoute>()
            GalleryScreen(
                images = galleryModel.images,
                startIndex = galleryModel.startIndex,
                destiny = galleryModel.destiny,
                businessName = galleryModel.businessName,
                onBackPressed = { navController.popBackStack() }
            )
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
                    // 2026-08-20 (design handoff pass): land on the new "16. Listo" success
                    // screen first -- this used to jump straight to the order-detail screen,
                    // which the handoff treats as a separate, later destination (task 26's
                    // "19. Detalles de la reserva"), not a payment confirmation.
                    navController.navigate(
                        PaymentSuccessScreenRoute(
                            tripTitle = order.tripTitle,
                            leavingDate = order.dateFrom,
                            confirmationCode = order.transactionId,
                            amountPaid = order.amount,
                        )
                    ) {
                        popUpTo(ShoppingNavigationRoutes.ShoppingItemDetailScreen.route) { inclusive = true }
                    }
                }, onPaymentFailed = { reason, amount ->
                    navController.navigate(
                        PaymentFailedScreenRoute(
                            tripTitle = model.title,
                            amount = amount,
                            reason = reason,
                        )
                    )
                })
        }
        composable<PaymentFailedScreenRoute> { backStackEntry ->
            val failedModel = backStackEntry.toRoute<PaymentFailedScreenRoute>()
            PaymentFailedScreen(
                tripTitle = failedModel.tripTitle,
                amount = failedModel.amount,
                reason = failedModel.reason,
                onRetry = { navController.popBackStack() },
                onChangePaymentMethod = { navController.popBackStack() },
                onBackToExplore = { onBackPressed() },
            )
        }
        composable<PaymentSuccessScreenRoute> { backStackEntry ->
            val successModel = backStackEntry.toRoute<PaymentSuccessScreenRoute>()
            PaymentSuccessScreen(
                tripTitle = successModel.tripTitle,
                leavingDate = successModel.leavingDate,
                confirmationCode = successModel.confirmationCode,
                amountPaid = successModel.amountPaid,
                onViewTrips = { onBackPressed() },
                onKeepExploring = { onBackPressed() },
            )
        }
        composable<ShoppingOrderDetailScreenRoute> { backStackEntry ->
            val model = backStackEntry.toRoute<ShoppingOrderDetailScreenRoute>()
            ShoppingOrderDetailScreen(
                model = model,
                rateOrder = { orderId, tripTitle, tripImage, date, providerName ->
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
                },
                onBackPressed = { onBackPressed() },
                reportOrder = {
                    navController.navigate(
                        OrderHistoryNavigation.ReportOrderIssuesScreen(
                            orderId = model.transactionId,
                            tripTitle = model.tripTitle,
                        )
                    )
                })
        }
        composable<OrderHistoryNavigation.ReportOrderIssuesScreen> { backStackEntry ->
            val model = backStackEntry.toRoute<OrderHistoryNavigation.ReportOrderIssuesScreen>()

            ReportOrderIssueScreen(
                orderId = model.orderId,
                tripTitle = model.tripTitle,
                onBackPressed = {
                    navController.popBackStack()
                })
        }
    }
}