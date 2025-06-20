package com.agusteam.caribeando.presenter.home.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes(val route: String) {
    data object HomeScreen : NavigationRoutes("home_screen")
    data object ProfileScreen : NavigationRoutes("profile_screen")
    data object WishListScreen : NavigationRoutes("wishlist_screen")
    data object OrderHistoryScreen : NavigationRoutes("order_history_screen")
}

@Serializable
data class TripDetailScreenRoute(
    val tripId: String,
    val name: String,
    val lat: Float,
    val lng: Float,
    val businessId: String = "",
    val businessName: String = "",
    val month: Int = 0,
    val isFavorite: Boolean,
    val arrivingTime: String = "",
    val leavingTime: String = "",
    val meetingPoint: String = "",
    val price: Int = 0,
    val initialPayment: Int = 0,
    val destiny: String = "",
    val tripScheduleId: String = "",
    val reviewCount: Int = 0,
    val rating: Float = 0f

)

