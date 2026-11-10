package com.agusteam.caribeando.presenter.signup.navigation

import com.agusteam.caribeando.presenter.home.navigation.CaribeandoTransitions
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.agusteam.caribeando.presenter.home.navigation.TripDetailScreenRoute
import com.agusteam.caribeando.presenter.home.screen.HomeScreen
import com.agusteam.caribeando.presenter.shopping.navigation.ShoppingFlowNavigation
import com.agusteam.caribeando.presenter.signup.screen.FillSignUpAccountScreen
import com.agusteam.caribeando.presenter.signup.screen.ForgotPasswordScreen
import com.agusteam.caribeando.presenter.signup.screen.LoginScreen
import com.agusteam.caribeando.presenter.signup.screen.SignUpAccountScreen
import com.agusteam.caribeando.presenter.theme.CaribeandoTheme
import com.agusteam.caribeando.presenter.theme.caribeandoColors


@Composable
fun MainNavigationFlow() {
    val navController = rememberNavController()
    CaribeandoTheme {
    // 2026-08-20 (dark-mode audit): this root Box used the raw, theme-blind `backGround`
    // val (always white) -- since `.background()` is applied before `.padding()`, this painted
    // a permanent solid-white strip top/bottom of literally every screen in the app whenever the
    // system is in dark mode. Switched to `colors.pageBackground`, which is white in light mode
    // and the handoff's dark page color in dark mode.
    val colors = caribeandoColors
    Box(Modifier.fillMaxSize().background(colors.pageBackground).padding(vertical = 16.dp)) {
        NavHost(
            navController = navController, startDestination = SignupNavigationRoutes.HomeScreen,
            // 2026-10-09 (design handoff, "Transitions"): push/pop = 300ms fade + slide.
            enterTransition = { CaribeandoTransitions.enter },
            exitTransition = { CaribeandoTransitions.exit },
            popEnterTransition = { CaribeandoTransitions.popEnter },
            popExitTransition = { CaribeandoTransitions.popExit },
        ) {
            composable(SignupNavigationRoutes.LoginScreen.route) {
                LoginScreen(onLogin = { model ->
                    if ((!model.isPhoneConfigured || !model.isBirthdateConfigured)) {
                        navController.navigate(SignupNavigationRoutes.FillUserInfo.route)
                    } else {
                        navController.navigate(SignupNavigationRoutes.HomeScreen)
                    }
                }, onSignUp = {
                    navController.navigate(SignupNavigationRoutes.SignUpCreateScreen.route)
                }, onForgotPassword = {
                    navController.navigate(SignupNavigationRoutes.ForgotPasswordScreen.route)
                })
            }
            // 2026-08-20 (design handoff pass): "¿Olvidó su contraseña?" -- new standalone screen,
            // see ForgotPasswordScreen.kt.
            composable(SignupNavigationRoutes.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(
                    onBackPressed = { navController.popBackStack() },
                    onLinkSent = { navController.popBackStack() }
                )
            }
            composable(SignupNavigationRoutes.SignUpCreateScreen.route) {
                SignUpAccountScreen(
                    onBackPressed = { navController.popBackStack() },
                    onSignUpSuccess = {
                        navController.navigate(SignupNavigationRoutes.HomeScreen)
                    })
            }
            composable(SignupNavigationRoutes.FillUserInfo.route) {
                FillSignUpAccountScreen(
                    onBackPressed = { navController.popBackStack() },
                    onLogin = {
                        navController.navigate(SignupNavigationRoutes.HomeScreen)
                    })
            }
            composable<SignupNavigationRoutes.HomeScreen> {
                HomeScreen(onNavigateDetails = { route ->
                    navController.navigate(route)
                }, logout = {
                    navController.navigate(SignupNavigationRoutes.LoginScreen.route) {
                    }
                }, onIncompleteProfile = {
                    // 2026-08-20 (design handoff pass): the handoff assumed `Token.isConfirmed`
                    // meant "email verified" and asked for a new "Cuenta sin confirmar" screen with
                    // a resend-email action. It actually means "phone + birthdate configured"
                    // (LoginRepositoryImpl sets it from isPhoneConfigured && isBirthdateConfigured)
                    // -- there's no email-verification concept anywhere in this backend. The real
                    // fix for "dead-ends the user" is routing to the screen that already collects
                    // exactly what's missing (FillSignUpAccountScreen), the same screen login/signup
                    // already push to when those fields are missing -- not a new screen that tells
                    // the user to check an email that was never sent. See the chat response for the
                    // full note; RequestResetPasswordEmailUseCase/email-confirmation is unrelated.
                    navController.navigate(SignupNavigationRoutes.FillUserInfo.route)
                })
            }
            composable<TripDetailScreenRoute> { backStackEntry ->
                val model = backStackEntry.toRoute<TripDetailScreenRoute>()
                ShoppingFlowNavigation(
                    modelRoute = model,
                    onBackPressed = { navController.navigate(SignupNavigationRoutes.HomeScreen) })
            }
        }
    }
}
}
