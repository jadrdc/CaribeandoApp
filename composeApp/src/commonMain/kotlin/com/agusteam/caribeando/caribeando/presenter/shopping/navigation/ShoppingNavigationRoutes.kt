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
    val leavingTime: String = "",
    val meetingPoint: String = "",
    val initialPayment: Int = 0,
    val totalPayment: Int = 0,
    val tripDetailId: String,
    val arrivingTime: String,

    val businessName: String,
    val businessMonth: String,
    val tripId: String = ""
)

@Serializable
data class ShoppingOrderDetailScreenRoute(
    val tripTitle: String,
    val amount: Int,
    val dateFrom: String,
    val dateTo: String,
    val transactionId: String,
    val orderId: String = "",
    val businessName: String,
    val businessPhoto: String,
    val businessMonth: String,
    var hasItBeenEvaluated: Boolean = true,
    val galleryPhoto: String,
    val scheduledId: String,
    var isPending: Boolean = false,
    // Gates whether the "Cancel booking" action is shown — only upcoming (not-yet-departed)
    // bookings can be cancelled. Added 2026-08-18 with the cancellation/refund flow.
    val isUpcoming: Boolean = false,
    // 2026-08-20 (design handoff pass): real amount already paid on this order (backend gap #1
    // -- Order.amount vs TripScheduled.totalPayment). Mutable so a successful "Pagar faltante"
    // (screen 20) can update it in place without a full re-fetch. Drives the conditional
    // "Falta por pagar" panel: shown when paidAmount < amount.
    var paidAmount: Int = 0,
)

@Serializable
// 2026-08-20 (design handoff pass): extended with real trip-header fields (screen 24's
// "64x64 thumb + title/meta" header) -- both call sites (Viajes pasados list, order-detail
// screen) already have this data in scope, so it's threaded through the route rather than
// re-fetched or faked.
data class RatingTripRoute(
    val orderId: String,
    val isFromList: Boolean,
    val tripTitle: String = "",
    val tripImage: String = "",
    val date: String = "",
    val providerName: String = "",
)

// 2026-08-20 (design handoff pass): route for the new "13. Galería" full-screen photo viewer,
// opened by tapping the page-counter pill on the trip-detail hero.
@Serializable
data class GalleryScreenRoute(
    val images: List<String>,
    val startIndex: Int,
    val destiny: String,
    val businessName: String,
)

// 2026-08-20 (design handoff pass): route for the new "16. Listo" success screen, inserted
// between a completed Stripe payment and the (task-26) order-detail screen.
@Serializable
data class PaymentSuccessScreenRoute(
    val tripTitle: String,
    val leavingDate: String,
    val confirmationCode: String,
    val amountPaid: Double,
)

// 2026-08-20 (design handoff pass): route for the new "17. Pago fallido" recovery screen.
@Serializable
data class PaymentFailedScreenRoute(
    val tripTitle: String,
    val amount: Double,
    val reason: String,
)

// 2026-08-20 (design handoff pass): route for the new "20. Pagar faltante" screen -- paying
// the remaining balance on an already-SUCCESS order. Reuses PaymentViewModel/StripeButton via
// PaymentEvents.InitialLoad(existingOrderId = orderId, ...).
@Serializable
data class PayBalanceScreenRoute(
    val orderId: String,
    val transactionId: String,
    val tripTitle: String,
    val businessName: String,
    val businessPhoto: String,
    val totalPayment: Int,
    val paidAmount: Int,
)
