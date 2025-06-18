package com.agusteam.caribeando.presenter.signup.navigation

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
import com.agusteam.caribeando.presenter.signup.screen.LoginScreen
import com.agusteam.caribeando.presenter.signup.screen.SignUpAccountScreen
import com.agusteam.caribeando.presenter.theme.backGround


@Composable
fun MainNavigationFlow() {
    val navController = rememberNavController()
    Box(Modifier.fillMaxSize().background(backGround).padding(vertical = 16.dp)) {
        NavHost(
            navController = navController, startDestination = SignupNavigationRoutes.HomeScreen
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
                })
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