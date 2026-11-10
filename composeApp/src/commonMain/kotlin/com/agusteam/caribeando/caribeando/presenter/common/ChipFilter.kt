package com.agusteam.caribeando.presenter.common

import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * The handoff's "Chip": "Radius 999, padding 8 x 14, 6dp gap, icon 15, label 500/12.5.
 * Unselected: 1dp #E2E2E2 border, #003049 content. Selected: #003049 fill, white content."
 * Used for category chips (Explorar), budget/date chips (Filtrar sheet).
 *
 * 2026-08-20 (design handoff pass): replaces the never-wired-up FilterChip-based version of this
 * file -- it had zero call sites anywhere in the app, and its `onClick` was a hardcoded no-op
 * `{ }` (a real dead-click bug nobody could have hit yet, since nothing called it). This is a
 * from-scratch implementation matching the handoff spec exactly, since M3's FilterChip doesn't
 * expose the padding/radius/color combination the spec wants.
 *
 * Two icon params, both optional: [icon] for the handoff's local vector drawables (ic_beach,
 * ic_mountain, ...), [iconUrl] for the backend's real per-category image URL (`CategoryModel
 * .image`, loaded via Coil) -- real category data comes from the server as a URL, not a bundled
 * drawable, so category chips need the remote path even though the handoff's spec sample only
 * shows the 4 local drawables it happened to design against. [icon] wins if both are set.
 */
@Composable
fun Chip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: DrawableResource? = null,
    iconUrl: String? = null,
) {
    val colors = caribeandoColors
    val contentColor = if (isSelected) Color.White else colors.navyFill
    Row(
        // 2026-10-09 (design handoff, "Known deviation to fix"): chips are 34dp tall, below the
        // 44-48dp minimum touch target. The tap handler now sits OUTSIDE
        // minimumInteractiveComponentSize(), so the touch area grows to 48dp while the drawn pill
        // (background/border/padding after it) keeps its 34dp visual size.
        modifier = Modifier
            .bounceClick(scaleDown = 0.94f) { onClick() }
            .minimumInteractiveComponentSize()
            .background(
                if (isSelected) colors.navyFill else Color.Transparent,
                Radii.pillShape
            )
            .then(
                if (!isSelected) Modifier.border(1.dp, colors.panelFieldBorder, Radii.pillShape) else Modifier
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(15.dp)
            )
        } else if (iconUrl != null) {
            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(contentColor),
                modifier = Modifier.size(15.dp)
            )
        }
        Text(
            text = title,
            style = caribeandoTextStyles().chipLabel,
            color = contentColor
        )
    }
}
