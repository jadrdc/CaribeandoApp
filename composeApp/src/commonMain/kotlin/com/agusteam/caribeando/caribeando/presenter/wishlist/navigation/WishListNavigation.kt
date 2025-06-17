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
    val tripId: String,
    val name: String,
    val lat: Float,
    val lng: Float,
    val description: String = "",
    val businessId: String = "",
    val businessImage: String = "",
    val businessName: String = "",
    val month: Int = 0,
    val isFavorite: Boolean,
    val cancellationPolicy: String,

    val arrivingTime: String,
    val leavingTime: String,
    val meetingPoint: String,
    val price: Int,
    val initialPayment: Int,
    val destiny: String,
    val galleryPhoto: List<String>
)
