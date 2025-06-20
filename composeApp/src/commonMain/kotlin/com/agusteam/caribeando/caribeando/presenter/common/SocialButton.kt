package com.agusteam.caribeando.presenter.common

import androidx.compose.runtime.Composable
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.signup.viewmodels.AppleSignUpViewModel

@Composable
expect fun SocialButton(
    onLogin: (TokenMode) -> Unit
)