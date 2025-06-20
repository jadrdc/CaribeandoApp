package com.agusteam.caribeando.presenter.wishlist.navigation

import kotlinx.serialization.Serializable

sealed class WishListNavigation(val route: String) {
    data object WishListScreen : WishListNavigation("wish_list_screen")
}

@Serializable
data class WishProviderProfileScreen(
    val businessId: String
)


@Serializable
data class WishListItemDetailScreenRoute(
    val tripId: String = "",
    val name: String = "",
    val lat: Float = 0.0f,
    val lng: Float = 0.0f,
    val businessId: String = "",
    val businessName: String = "",
    val month: Int = 0,
    val isFavorite: Boolean = true,
    val arrivingTime: String = "",
    val leavingTime: String = "",
    val meetingPoint: String = "",
    val price: Int = 0,
    val initialPayment: Int = 0,
    val destiny: String = ""
)
