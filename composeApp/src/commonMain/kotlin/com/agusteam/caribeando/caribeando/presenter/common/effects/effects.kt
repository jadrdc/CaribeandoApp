package com.agusteam.caribeando.presenter.common.effects

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.agusteam.caribeando.presenter.theme.caribeandoColors

// 2026-08-20 (dark-mode audit): this shared shimmer/skeleton loader hardcoded light-mode grey
// literals even though `CaribeandoExtendedColors` already defines exactly this role pair
// (`shimmerBase`/`shimmerHighlight`) with proper dark-mode values -- every loading skeleton in
// the app that uses this modifier (e.g. `SearchBar.kt`) stayed light-grey in dark mode instead
// of the intended dark-navy-grey tones.
fun Modifier.shimmerEffect(): Modifier = composed {
    val colors = caribeandoColors
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                colors.shimmerBase,
                colors.shimmerHighlight,
                colors.shimmerBase,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}