package com.agusteam.caribeando.presenter.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.presenter.common.BottomNavigationBar
import com.agusteam.caribeando.presenter.explore.screen.ExploreScreen
import com.agusteam.caribeando.presenter.home.navigation.NavigationRoutes
import com.agusteam.caribeando.presenter.home.navigation.TripDetailScreenRoute
import com.agusteam.caribeando.presenter.home.state.HomeOption
import com.agusteam.caribeando.presenter.home.viewmodel.HomeViewModel
import com.agusteam.caribeando.presenter.orders.navigation.OrderHistoryNavigationFlow
import com.agusteam.caribeando.presenter.profile.screen.ProfileScreen
import com.agusteam.caribeando.presenter.theme.CustomTypography
import com.agusteam.caribeando.presenter.theme.backGround
import com.agusteam.caribeando.presenter.wishlist.navigation.WishListNavigationFlow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
@Preview
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateDetails: (TripDetailScreenRoute) -> Unit,
    logout: () -> Unit
) {
    val navController = rememberNavController()
    val homeState = viewModel.state.collectAsStateWithLifecycle().value


    MaterialTheme(typography = CustomTypography()) {
        Scaffold(
            bottomBar = {
                if (homeState.currentNavigationOption != HomeOption.SHOPPING_ITEM_DETAIL) BottomNavigationBar(
                    navController = navController, visible = true
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) { innnerPadding ->
            Box(
                modifier = Modifier.padding(innnerPadding) // ✅ Corrección clave
                    .background(backGround).fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = NavigationRoutes.HomeScreen.route

                ) {
                    composable(
                        NavigationRoutes.HomeScreen.route
                    ) {
                        viewModel.handleEvent(HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.EXPLORE))
                        ExploreScreen { tripModel ->

                            if (Token.isValid && Token.isConfirmed) {
                                val route = TripDetailScreenRoute(
                                    destiny = tripModel.destiny,
                                    cancellationPolicy = tripModel.cancellation_policy,
                                    galleryPhotoJson = Json.encodeToString(tripModel.images), // 👈 serializa aquí
                                    tripId = tripModel.id,
                                    isFavorite = tripModel.isSavedForLater,
                                    month = tripModel.month,
                                    businessImage = tripModel.businessImage,
                                    businessName = tripModel.businessName,
                                    businessId = tripModel.businessId,
                                    name = tripModel.name,
                                    description = tripModel.description,
                                    lat = tripModel.lat.toFloat(),
                                    lng = tripModel.lng.toFloat(),
                                    initialPayment = tripModel.initialPayment.toInt(),
                                    meetingPoint = tripModel.meetingPoint,
                                    arrivingTime = tripModel.arrivingTime,
                                    leavingTime = tripModel.leavingTime,
                                    price = tripModel.price.toInt(),
                                    tripScheduleId = tripModel.tripScheduleId,
                                    reviewCount = tripModel.reviewCount,
                                    rating = tripModel.rating.toFloat()
                                )

                                onNavigateDetails(
                                    route
                                )
                            } else {
                                logout()
                            }
                        }
                    }
                    composable(NavigationRoutes.ProfileScreen.route) {
                        if (Token.isValid && Token.isConfirmed) {
                            viewModel.handleEvent(HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.PROFILE))
                            ProfileScreen(logout = logout)
                        } else {
                            logout()
                        }
                    }
                    composable(NavigationRoutes.WishListScreen.route) {
                        if (Token.isValid && Token.isConfirmed) {
                            viewModel.handleEvent(HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.WISHLIST))
                            WishListNavigationFlow()
                        } else {
                            logout()
                        }
                    }

                    composable(NavigationRoutes.OrderHistoryScreen.route) {
                        if (Token.isValid && Token.isConfirmed) {
                            OrderHistoryNavigationFlow(showBottomNav = {
                                if (it)
                                    viewModel.handleEvent(
                                        HomeViewModel.HomeEvent.ChangeHomeTab(
                                            HomeOption.TRIP
                                        )
                                    )
                                else
                                    viewModel.handleEvent(
                                        HomeViewModel.HomeEvent.ChangeHomeTab(
                                            HomeOption.SHOPPING_ITEM_DETAIL
                                        )
                                    )
                            })
                            LaunchedEffect(Unit) {
                                viewModel.handleEvent(
                                    HomeViewModel.HomeEvent.ChangeHomeTab(
                                        HomeOption.TRIP
                                    )
                                )
                            }
                        } else {
                            logout()
                        }
                    }
                }
            }
        }
    }
}
