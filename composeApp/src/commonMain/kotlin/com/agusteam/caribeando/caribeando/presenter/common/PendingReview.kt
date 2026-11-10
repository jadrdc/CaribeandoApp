package com.agusteam.caribeando.presenter.common

import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.review_pending
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 2026-10-09 (design handoff, dark-mode/palette pass): was a hardcoded orange
 * (#FFF3E0 / #FF9800) that isn't in the design system and didn't change in dark mode. Now uses
 * the handoff's red-tint panel + red-as-text roles (theme-aware), the shared Poppins metadata
 * style, and the copy lives in strings.xml.
 */
@Composable
fun PendingReview(onClick:()->Unit) {
    val colors = caribeandoColors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
            .background(colors.redTint, shape = RoundedCornerShape(16.dp))
            .padding(start = 10.dp, top = 4.dp, bottom = 4.dp, end = 2.dp)
    ) {
        ClockIcon(modifier = Modifier.size(18.dp), color = colors.redText)
        Spacer(Modifier.width(8.dp))
        Text(
            minLines = 2,
            maxLines = 2,
            modifier = Modifier.widthIn(max = 80.dp),
            text = stringResource(Res.string.review_pending),
            color = colors.redText,
            style = caribeandoTextStyles().metadataSmall
        )
    }
}


@Composable
fun ClockIcon(modifier: Modifier = Modifier, color: Color = Color(0xFFFF9800)) {
    Canvas(modifier = modifier.size(16.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 * 0.8f

        // Draw clock circle
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = size.minDimension * 0.1f)
        )

        // Draw hour hand (pointing to 2 o'clock)
        val hourAngle = 2.0 / 12.0 * 2.0 * PI
        drawLine(
            color = color,
            start = center,
            end = Offset(
                x = center.x + (radius * 0.5f) * cos(hourAngle).toFloat(),
                y = center.y + (radius * 0.5f) * sin(hourAngle).toFloat()
            ),
            strokeWidth = size.minDimension * 0.08f,
            cap = StrokeCap.Round
        )

        // Draw minute hand (pointing to 10)
        val minuteAngle = 10.0 / 12.0 * 2.0 * PI
        drawLine(
            color = color,
            start = center,
            end = Offset(
                x = center.x + (radius * 0.7f) * cos(minuteAngle).toFloat(),
                y = center.y + (radius * 0.7f) * sin(minuteAngle).toFloat()
            ),
            strokeWidth = size.minDimension * 0.06f,
            cap = StrokeCap.Round
        )
    }
}