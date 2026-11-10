package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.caribeandoColors

// 2026-08-20 (screens 1-17 fidelity pass): spec screen 12 calls this "36x36 back circle top
// left" -- was a flat 32dp, this component's only call site (TripItemBar, the detail-hero
// overlay), so corrected in place rather than adding a size param nobody else needs yet.
//
// 2026-08-20 (re-verification pass): the circle was opaque `Color.White`, not even the "rgba
// white, .94 alpha" the sibling FavoriteButton uses for the exact same detail-hero overlay
// context -- and neither dark-mode aware. Switched to `colors.overlayOnPhoto`, the theme token
// already defined for "a translucent circle sitting over photo content," which lightens
// correctly in dark mode.
@Composable
fun BackButton(onClick: () -> Unit) {
    val colors = caribeandoColors
    Box(Modifier.clip(CircleShape).size(36.dp).background(colors.overlayOnPhoto, CircleShape)) {
        BackIcon(Modifier.align(Alignment.Center)){onClick()}
    }
}