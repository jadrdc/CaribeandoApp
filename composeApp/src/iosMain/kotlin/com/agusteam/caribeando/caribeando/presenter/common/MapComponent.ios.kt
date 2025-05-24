package com.agusteam.caribeando.presenter.common

// shared/src/iosMain/kotlin/MapView.kt
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import com.agusteam.caribeando.LocalNativeViewFactory

@Composable
actual fun MapComponent(modifier: Modifier, lat: Double, lng: Double) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        factory = {
            factory.createMap(
                lat = lat, lng = lng
            )
        }
    )
}
