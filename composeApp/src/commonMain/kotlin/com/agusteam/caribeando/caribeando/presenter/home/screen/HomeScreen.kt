package com.agusteam.caribeando.presenter.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.agusteam.caribeando.presenter.home.navigation.CaribeandoTransitions
import com.agusteam.caribeando.presenter.home.navigation.HomeTabRequest
import com.agusteam.caribeando.presenter.home.navigation.NavigationRoutes
import com.agusteam.caribeando.presenter.home.navigation.TripDetailScreenRoute
import com.agusteam.caribeando.presenter.home.state.HomeOption
import com.agusteam.caribeando.presenter.home.viewmodel.HomeViewModel
import com.agusteam.caribeando.presenter.orders.navigation.OrderHistoryNavigationFlow
import com.agusteam.caribeando.presenter.profile.screen.ContactUsScreen
import com.agusteam.caribeando.presenter.profile.screen.ProfileScreen
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.toIntOrNullSafe
import com.agusteam.caribeando.presenter.wishlist.navigation.WishListNavigationFlow
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
@Preview
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateDetails: (TripDetailScreenRoute) -> Unit,
    logout: () -> Unit,
    // 2026-08-20 (design handoff pass): previously every one of these 4 gates below called
    // `logout()` for BOTH "not logged in" and "profile incomplete" -- but those are different
    // problems with different fixes. Token.isConfirmed means "phone + birthdate configured," not
    // "email verified" (see LoginRepositoryImpl / MainNavigationFlow's onIncompleteProfile
    // comment), so a logged-in-but-incomplete user should go finish their profile, not get bounced
    // to the login screen with no explanation.
    onIncompleteProfile: () -> Unit = logout,
) {
    val navController = rememberNavController()
    val homeState = viewModel.state.collectAsStateWithLifecycle().value
    val colors = caribeandoColors

    fun requireCompleteAccount(onReady: () -> Unit) {
        if (!Token.isValid) {
            logout()
        } else if (!Token.isConfirmed) {
            onIncompleteProfile()
        } else {
            onReady()
        }
    }

    // 2026-08-19: theming now applied once at the true app root (MainNavigationFlow via
    // CaribeandoTheme) instead of here — this used to be the only MaterialTheme wrapper in the
    // whole app, and it never set a colorScheme, so login/signup/booking screens (which sit
    // outside HomeScreen in the nav graph) never got the custom font or brand colors at all.
        Scaffold(
            bottomBar = {
                if (homeState.currentNavigationOption != HomeOption.SHOPPING_ITEM_DETAIL) BottomNavigationBar(
                    navController = navController, visible = true
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) { innnerPadding ->
            // 2026-08-20 (dark-mode audit): was the raw, theme-blind `backGround` val (always
            // white) -- this Box sits behind every tab (Explore/Wishlist/Orders/Profile), so a
            // fixed-white fill here painted the WHOLE app shell white in dark mode regardless of
            // each tab's own dark-aware content. Switched to `colors.pageBackground`.
            Box(
                modifier = Modifier.padding(innnerPadding) // ✅ Corrección clave
                    .background(colors.pageBackground).fillMaxSize()
            ) {
                // 2026-10-09 (design handoff, screen 16): honor a one-shot tab request (e.g.
                // "Ver mis viajes" on the payment success screen -> Viajes tab).
                LaunchedEffect(Unit) {
                    HomeTabRequest.consume()?.let { route -> navController.navigate(route) }
                }
                NavHost(
                    navController = navController,
                    startDestination = NavigationRoutes.HomeScreen.route,
                    // 2026-10-09 (design handoff, "Transitions"): this NavHost holds the four
                    // tab roots, which crossfade (siblings, not a stack).
                    enterTransition = { CaribeandoTransitions.tabEnter },
                    exitTransition = { CaribeandoTransitions.tabExit },
                    popEnterTransition = { CaribeandoTransitions.tabEnter },
                    popExitTransition = { CaribeandoTransitions.tabExit },
                ) {
                    composable(
                        NavigationRoutes.HomeScreen.route
                    ) {
                        viewModel.handleEvent(HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.EXPLORE))
                        ExploreScreen(
                            onProfileClick = {
                                // 2026-08-20 (design handoff pass): screen 6's header profile
                                // shortcut -- routed through the same requireCompleteAccount gate
                                // as the bottom-nav Perfil tab, then navigates the same NavHost.
                                requireCompleteAccount {
                                    navController.navigate(NavigationRoutes.ProfileScreen.route)
                                }
                            },
                            goDetails = { tripModel ->
                            requireCompleteAccount {
                                val route = TripDetailScreenRoute(
                                    destiny = tripModel.destiny,
                                    tripId = tripModel.id,
                                    isFavorite = tripModel.isSavedForLater,
                                    month = tripModel.month,
                                    businessName = tripModel.businessName,
                                    businessId = tripModel.businessId,
                                    name = tripModel.name,
                                    meetingPoint = tripModel.meetingPoint,
                                    arrivingTime = tripModel.arrivingTime,
                                    leavingTime = tripModel.leavingTime,
                                    tripScheduleId = tripModel.tripScheduleId,
                                    reviewCount = tripModel.reviewCount,
                                    lat = if (tripModel.lat.isFinite()) tripModel.lat.toFloat() else 0f,
                                    lng = if (tripModel.lng.isFinite()) tripModel.lng.toFloat() else 0f,
                                    initialPayment = tripModel.initialPayment.toIntOrNullSafe(),
                                    price = tripModel.price.toIntOrNullSafe(),
                                    rating = if (tripModel.rating.isFinite()) tripModel.rating.toFloat() else 0f,
                                )

                                onNavigateDetails(
                                    route
                                )
                            }
                        })
                    }
                    composable(NavigationRoutes.ProfileScreen.route) {
                        requireCompleteAccount {
                            viewModel.handleEvent(HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.PROFILE))
                        }
                        if (Token.isValid && Token.isConfirmed) {
                            ProfileScreen(
                                logout = logout,
                                // 2026-08-20 (design handoff pass, part 2): screen 28's three
                                // navigation rows -- Favoritos/Mis viajes are real tab switches
                                // on this same NavHost; Contáctanos is a real pushed screen
                                // (below), reusing SHOPPING_ITEM_DETAIL's existing "hide the
                                // bottom nav" semantics for a pushed, non-tab screen -- same
                                // mechanism the checkout flow already relies on, not a new one.
                                goFavorites = { navController.navigate(NavigationRoutes.WishListScreen.route) },
                                goTrips = { navController.navigate(NavigationRoutes.OrderHistoryScreen.route) },
                                goContactUs = {
                                    viewModel.handleEvent(
                                        HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.SHOPPING_ITEM_DETAIL)
                                    )
                                    navController.navigate(NavigationRoutes.ContactUsScreen.route)
                                },
                            )
                        }
                    }
                    composable(NavigationRoutes.ContactUsScreen.route) {
                        if (Token.isValid && Token.isConfirmed) {
                            ContactUsScreen(
                                onBackPressed = {
                                    viewModel.handleEvent(HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.PROFILE))
                                    navController.navigate(NavigationRoutes.ProfileScreen.route)
                                },
                                goMyTrips = { navController.navigate(NavigationRoutes.OrderHistoryScreen.route) },
                            )
                        }
                    }
                    composable(NavigationRoutes.WishListScreen.route) {
                        requireCompleteAccount {
                            viewModel.handleEvent(HomeViewModel.HomeEvent.ChangeHomeTab(HomeOption.WISHLIST))
                        }
                        if (Token.isValid && Token.isConfirmed) {
                            WishListNavigationFlow(
                                // 2026-08-20 (design handoff pass, part 2): screen 27's "Explorar
                                // excursiones" -- same pattern as OrderHistoryNavigationFlow's
                                // onExploreClick just below.
                                onExploreClick = {
                                    navController.navigate(NavigationRoutes.HomeScreen.route)
                                }
                            )
                        }
                    }

                    composable(NavigationRoutes.OrderHistoryScreen.route) {
                        requireCompleteAccount {}
                        if (Token.isValid && Token.isConfirmed) {
                            OrderHistoryNavigationFlow(
                                showBottomNav = {
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
                                },
                                // 2026-08-20 (design handoff pass): screen 25's "Explorar
                                // excursiones" empty-state action -- jumps back to the Explorar
                                // tab via the same NavHost/route this bottom-nav tab already uses.
                                onExploreClick = {
                                    navController.navigate(NavigationRoutes.HomeScreen.route)
                                }
                            )
                            LaunchedEffect(Unit) {
                                viewModel.handleEvent(
                                    HomeViewModel.HomeEvent.ChangeHomeTab(
                                        HomeOption.TRIP
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
}
