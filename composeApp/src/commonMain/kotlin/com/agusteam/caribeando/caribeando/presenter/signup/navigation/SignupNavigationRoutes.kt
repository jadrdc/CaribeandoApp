package com.agusteam.caribeando.presenter.signup.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SignupNavigationRoutes(val route: String) {
    data object LoginScreen : SignupNavigationRoutes("login_screen")
    data object SignUpCreateScreen : SignupNavigationRoutes("sign_up_screen")

    @Serializable
    data object HomeScreen : SignupNavigationRoutes("home_screen")

    @Serializable
    data object FillUserInfo : SignupNavigationRoutes("fill_user")

    // 2026-08-20 (design handoff pass): "¿Olvidó su contraseña?" -- previously not a real screen,
    // just an inline trigger on LoginScreen reusing its own email field.
    @Serializable
    data object ForgotPasswordScreen : SignupNavigationRoutes("forgot_password_screen")
}