package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
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
    val factory = LocalNativeViewFactory.current
    val viewModel: AppleSignUpViewModel = koinViewModel()
    val event = viewModel.events

    ObserveAsEvents(event) { event ->
        if (event is AppleEvent.Success) {
            onLogin(event.data)
        }
    }
    UIKitViewController(
        modifier = Modifier.fillMaxWidth()
            .height(52.dp),
        factory = {
            factory.createSocialButton(
                onToken = { token, name, lastName ->
                    viewModel.onEventHandler(AppleEvent.SignUp(token, name, lastName))
                },
                onError = { error -> println("Apple Sign-In Error: $error") }
            )
        }
    )
}