package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A reusable "1dp dashed border, radius N" per the handoff's several dashed-block callouts
 * (confirmation-code blocks on screens 16/19, textarea outlines, etc.) -- Compose has no
 * built-in dashed-border modifier, so this draws one directly with a dash `PathEffect`.
 *
 * 2026-08-20 (design handoff pass): new, first used by the "16. Listo" confirmation-code block.
 */
fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: Dp = 16.dp,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 4.dp,
): Modifier = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f)
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
        size = Size(size.width - stroke.width, size.height - stroke.width),
        topLeft = androidx.compose.ui.geometry.Offset(stroke.width / 2, stroke.width / 2)
    )
}
