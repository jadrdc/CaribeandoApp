package com.agusteam.caribeando.presenter.wishlist.navigation

import com.agusteam.caribeando.presenter.home.navigation.CaribeandoTransitions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.agusteam.caribeando.presenter.orders.screen.OrderItemDetailsScreen
import com.agusteam.caribeando.presenter.profile.screen.TripProviderProfileScreen
import com.agusteam.caribeando.presenter.wishlist.screen.WishListScreen

@Composable
fun WishListNavigationFlow(
    // 2026-08-20 (design handoff pass, part 2): threaded down to WishListScreen for screen 27's
    // "Explorar excursiones" empty-state action.
    onExploreClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    var route = remember {
        WishListItemDetailScreenRoute(
        )
    }

    NavHost(
        navController = navController,
        startDestination = WishListNavigation.WishListScreen.route,
        // 2026-10-09 (design handoff, "Transitions"): push/pop = 300ms fade + slide.
        enterTransition = { CaribeandoTransitions.enter },
        exitTransition = { CaribeandoTransitions.exit },
        popEnterTransition = { CaribeandoTransitions.popEnter },
        popExitTransition = { CaribeandoTransitions.popExit },
    ) {
        composable(WishListNavigation.WishListScreen.route) {
            WishListScreen(onExploreClick = onExploreClick) { tripModel ->
                route = WishListItemDetailScreenRoute(
                    destiny = tripModel.destiny,
                    tripId = tripModel.id,
                    isFavorite = tripModel.isSavedForLater,
                    month = tripModel.month,
                    businessName = tripModel.businessName,
                    businessId = tripModel.businessId,
                    name = tripModel.name,
                    lat = tripModel.lat.toFloat(),
                    lng = tripModel.lng.toFloat(),
                    initialPayment = tripModel.initialPayment.toInt(),
                    meetingPoint = tripModel.meetingPoint,
                    arrivingTime = tripModel.arrivingTime,
                    leavingTime = tripModel.leavingTime,
                    price = tripModel.price.toInt(),
                )
                navController.navigate(route)
            }
        }
        composable<WishListItemDetailScreenRoute> { backStackEntry ->
            val model = backStackEntry.toRoute<WishListItemDetailScreenRoute>()
            OrderItemDetailsScreen(
                model = model,
                onBackPressed = {
                    navController.navigate(WishListNavigation.WishListScreen.route)
                }, goDetails = { businessId ->
                    navController.navigate(WishProviderProfileScreen(businessId))
                })
        }
        composable<WishProviderProfileScreen> { backStackEntry ->
            val tripProviderModel = backStackEntry.toRoute<WishProviderProfileScreen>()
            TripProviderProfileScreen(businessId = tripProviderModel.businessId, onBackPressed = {
                navController.navigate(route)
            })
        }
    }

}