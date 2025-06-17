package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import com.agusteam.caribeando.LocalNativeViewFactory
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.signup.viewmodels.AppleEvent
import com.agusteam.caribeando.presenter.signup.viewmodels.AppleSignUpViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun SocialButton(onLogin: (TokenMode) -> Unit) {
    val viewModel: AppleSignUpViewModel = koinViewModel()
    val latestOnLogin by rememberUpdatedState(onLogin)
    val factory = LocalNativeViewFactory.current // ✅ composable call outside remember

    val controller = remember(factory) {
        factory.createSocialButton(
            onToken = { token, name, lastName ->
                viewModel.onEventHandler(AppleEvent.SignUp(token, name, lastName))
            },
            onError = { error -> println("Apple Sign-In Error: $error") }
        )
    }

    ObserveAsEvents(viewModel.events) { event ->
        if (event is AppleEvent.Success) {
            latestOnLogin(event.data)
        }
    }

    UIKitViewController(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        factory = { controller }
    )
}
