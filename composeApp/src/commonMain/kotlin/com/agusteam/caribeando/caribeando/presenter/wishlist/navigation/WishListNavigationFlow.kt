package com.agusteam.caribeando.presenter.wishlist.navigation

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
fun WishListNavigationFlow() {
    val navController = rememberNavController()
    var route = remember {
        WishListItemDetailScreenRoute(
            "", "",
            0.0f, 0.0f,
            "", "", "", "", 0, isFavorite = true,
            "", "", "", "", 0, 0, "",
            galleryPhoto = listOf()
        )
    }

    NavHost(
        navController = navController,
        startDestination = WishListNavigation.WishListScreen.route
    ) {
        composable(WishListNavigation.WishListScreen.route) {
            WishListScreen { tripModel ->
                route = WishListItemDetailScreenRoute(
                    destiny = tripModel.destiny,
                    cancellationPolicy = tripModel.cancellation_policy,
                    tripId = tripModel.id,
                    isFavorite = tripModel.isSavedForLater,
                    month = tripModel.month,
                    businessImage = tripModel.businessImage,
                    businessName = tripModel.businessName,
                    businessId = tripModel.businessId,
                    name = tripModel.name,
                    description = tripModel.description,
                    lat = tripModel.lat.toFloat(),
                    galleryPhoto = tripModel.images, // 👈 serializa aquí
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