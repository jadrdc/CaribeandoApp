package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.location

/**
 * The handoff's map block (spec screen 12): the map at 150 tall, radius 16.
 *
 * 2026-08-20 (design handoff pass): `MapComponent`'s android/iOS `actual` implementations both
 * hardcode their own internal height (250dp), which would normally fight a caller-supplied
 * height modifier -- but Compose coerces a child's `.height()` request down to its parent's max
 * constraint, so wrapping the call in a fixed 150dp Box here is enough to get the spec height
 * without touching either platform's map implementation.
 */
@Composable
fun MapDetails(lat: Double, lng: Double) {
    val colors = caribeandoColors
    Column(Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = stringResource(Res.string.location),
            color = colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            MapComponent(Modifier.fillMaxWidth().height(150.dp), lat = lat, lng = lng)
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}
