package com.agusteam.caribeando.presenter.home.model


import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import caribeando.composeapp.generated.resources.explore
import caribeando.composeapp.generated.resources.favorite
import caribeando.composeapp.generated.resources.profile
import caribeando.composeapp.generated.resources.trips_title
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_explore
import caribeando.composeapp.generated.resources.ic_profile
import caribeando.composeapp.generated.resources.ic_trip
import caribeando.composeapp.generated.resources.ic_wish_list

sealed class BottomNavigationItem(
    val title: StringResource,
    val drawableResource: DrawableResource,
    val route: String
) {
    data object Explore : BottomNavigationItem(Res.string.explore, Res.drawable.ic_explore, "home_screen")
    data object WishList :
        BottomNavigationItem(Res.string.favorite, Res.drawable.ic_wish_list, "wishlist_screen")

    data object OrderHistoric :
        BottomNavigationItem(Res.string.trips_title, Res.drawable.ic_trip, "order_history_screen")

    data object Profile : BottomNavigationItem(Res.string.profile, Res.drawable.ic_profile, "profile_screen")
}