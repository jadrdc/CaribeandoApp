package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.primary
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun MapComponent(modifier: Modifier, lat: Double, lng: Double) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
    ) {
        val coordinates = LatLng(lat, lng)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(coordinates, 15f)
        }
        val properties by remember {
            mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
        }

        GoogleMap(
            properties = properties,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            cameraPositionState = cameraPositionState,
            uiSettings = com.google.maps.android.compose.MapUiSettings(
                zoomControlsEnabled = false // Disable zoom controls
            )
        ) {
            Circle(
                fillColor = primary.copy(alpha = 0.3f), // Set fill color with 50% alpha
                strokeColor = Color.Unspecified,
                strokeWidth = 1f,
                radius = 300.0,
                center = coordinates
            )
        }
    }
}