package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.agusteam.caribeando.LocalNativeViewFactory
import com.agusteam.caribeando.domain.models.TokenMode

@Composable
actual fun SocialButton(onLogin: (TokenMode) -> Unit) {
    val factory = LocalNativeViewFactory.current

    Column() {
        Text("TEST")
        UIKitViewController(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                factory.createSocialButton(
                    onToken = { token -> onLogin(TokenMode(token, "apple")) },
                    onError = { error -> println("Apple Sign-In Error: $error") }
                )
            }
        )
    }
}