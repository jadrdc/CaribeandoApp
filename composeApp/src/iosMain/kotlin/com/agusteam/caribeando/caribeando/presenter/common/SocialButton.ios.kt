package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import com.agusteam.caribeando.LocalNativeViewFactory
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.signup.viewmodels.AppleEvent
import com.agusteam.caribeando.presenter.signup.viewmodels.AppleSignUpViewModel
import com.agusteam.caribeando.presenter.theme.primary
import org.koin.compose.viewmodel.koinViewModel
import platform.UIKit.UIViewController

@Composable
actual fun SocialButton(
    onLogin: (TokenMode) -> Unit
) {
    val viewModel: AppleSignUpViewModel = koinViewModel()
    val latestOnLogin by rememberUpdatedState(onLogin)
    val factory = LocalNativeViewFactory.current // ✅ composable call outside remember
    var nativeController by remember { mutableStateOf<UIViewController?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        if (event is AppleEvent.Success) {
            latestOnLogin(event.data)
        }
    }

    LaunchedEffect(Unit) {
        nativeController = factory.createSocialButton(
            onToken = { token, name, lastName ->
                viewModel.onEventHandler(AppleEvent.SignUp(token, name, lastName))
            },
            onError = { error -> println("Apple Sign-In Error: $error") }
        )
    }

    if (nativeController != null) {
        UIKitViewController(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            factory = { nativeController!! }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primary)
        }
    }
}
