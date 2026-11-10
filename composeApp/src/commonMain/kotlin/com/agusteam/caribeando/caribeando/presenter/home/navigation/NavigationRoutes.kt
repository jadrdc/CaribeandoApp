package com.agusteam.caribeando.presenter.home.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes(val route: String) {
    data object HomeScreen : NavigationRoutes("home_screen")
    data object ProfileScreen : NavigationRoutes("profile_screen")
    data object WishListScreen : NavigationRoutes("wishlist_screen")
    data object OrderHistoryScreen : NavigationRoutes("order_history_screen")
    // 2026-08-20 (design handoff pass, part 2): screen 30, "Contáctanos" -- pushed from Perfil,
    // lives in this same NavHost (see HomeScreen.kt) since it needs no route params.
    data object ContactUsScreen : NavigationRoutes("contact_us_screen")
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

