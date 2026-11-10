package com.agusteam.caribeando.presenter.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_favorite
import caribeando.composeapp.generated.resources.ic_favorite_filled

/**
 * 2026-08-19 (interactivity pass): the heart icon now "pops" -- snapping down and springing back
 * up past 1f before settling -- every time it becomes favorited, the same bounce-on-like feedback
 * used by most social/booking apps (Instagram, Twitter/X-style like buttons). The outer circle
 * keeps its own bounceClick press-scale, with indication = null preserved since this button floats
 * over card imagery and a ripple there looked wrong even before this pass.
 *
 * 2026-08-20 (screens 1-17 fidelity pass): the handoff's "TripCard (feed)" spec calls this circle
 * "34x34... rgba(255,255,255,.94)... ic_favorite_filled 18 #D62828... ic_favorite 18 #003049
 * when not [saved]" -- it was a flat 32dp opaque-white circle with a 24dp icon tinted
 * `blackColor` when unsaved. [size] now defaults to that feed-card 34dp so existing call sites
 * are unaffected in spirit while actually matching the spec; screen 12's detail-hero heart
 * (spec: "36x36... top right") passes size = 36.dp explicitly. Icon stays fixed at 18dp per the
 * handoff's global icon-size table ("17-18 search / heart / chevron-adjacent") regardless of the
 * outer circle's size -- only the circle grows between the two call sites, not the glyph.
 *
 * 2026-08-20 (re-verification pass): two dark-mode gaps found on independent re-review. The
 * circle's rgba(255,255,255,.94) background was hardcoded as `Color.White.copy(alpha = 0.94f)`
 * instead of the theme's own `colors.overlayOnPhoto` token, which the handoff's color table
 * defines as a DIFFERENT color in dark mode (`rgba(12,26,34,.88)`) for exactly this "sits over a
 * photo" role -- the hardcoded white ignored dark theme entirely. The saved-heart tint used
 * `colors.redFill` instead of `colors.redText`: per the handoff's own "Design tokens" table
 * ("Red as a fill" stays `#D62828` in both themes; "Red as text or icon" lightens to `#FF6E6E`
 * in dark because the brand red fails WCAG AA as an icon color on a dark ground) this heart is
 * an icon, not a filled surface, so it belongs on the `redText` role.
 */
@Composable
fun FavoriteButton(
    isSavedForLater: Boolean,
    size: Dp = 34.dp,
    onFavoriteToggle: () -> Unit
) {
    val colors = caribeandoColors
    val popScale = remember { Animatable(1f) }
    LaunchedEffect(isSavedForLater) {
        if (isSavedForLater) {
            popScale.snapTo(0.7f)
            popScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Box(
        Modifier.clip(CircleShape).size(size).background(colors.overlayOnPhoto, CircleShape)
            .bounceClick(scaleDown = 0.85f, indication = null) {
                onFavoriteToggle()
            }) {
        Icon(
            tint = if (isSavedForLater) colors.redText else colors.textPrimary,
            modifier = Modifier.size(18.dp).align(Alignment.Center).scale(popScale.value),
            contentDescription = null,
            painter = painterResource(
                if (isSavedForLater) Res.drawable.ic_favorite_filled
                else Res.drawable.ic_favorite
            ),
        )
    }
}
