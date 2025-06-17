package com.agusteam.caribeando.presenter.shopping.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ShoppingNavigationRoutes(val route: String) {
    data object ShoppingItemDetailScreen : ShoppingNavigationRoutes("shopping_item_detail")
    //data object ShoppingItemPayingScreen : ShoppingNavigationRoutes("shopping_item_paying")

}

@Serializable
data class ProviderProfileScreen(
    val businessId: String
)

@Serializable
data class ShoppingItemPayingScreen(
    val title: String = "",
    val destiny: String = "",
    val profilePhoto: String = "",
    val leavingTime: String = "",
    val meetingPoint: String = "",
    val initialPayment: Int = 0,
    val totalPayment: Int = 0,
    val tripDetailId: String,
    val arrivingTime: String,

    val businessName: String,
    val businessPhoto: String,
    val businessMonth: String,
    val galleryPhotoJson: String = "[]", // 👈 Cambia a string con JSON

)

@Serializable
data class ShoppingOrderDetailScreenRoute(
    val tripTitle: String,
    val amount: Int,
    val dateFrom: String,
    val dateTo: String,
    val transactionId: String,
    val businessName: String,
    val businessPhoto: String,
    val businessMonth: String,
    var hasItBeenEvaluated: Boolean = true,
    val galleryPhoto: List<String> = listOf(),
    val scheduledId: String,
    var isPending: Boolean = false,
    val galleryPhotoJson: String = "[]", // 👈 Cambia a string con JSON
)

@Serializable
data class RatingTripRoute(val orderId: String, val isFromList: Boolean)