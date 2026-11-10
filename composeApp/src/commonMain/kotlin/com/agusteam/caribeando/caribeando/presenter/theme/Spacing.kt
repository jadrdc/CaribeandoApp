package com.agusteam.caribeando.presenter.theme

import androidx.compose.ui.unit.dp

/**
 * 2026-08-19: added as part of the UI consistency pass. Paddings/gaps across the app were ad hoc
 * dp literals scattered per screen with no shared scale, a common source of the "almost but not
 * quite consistent" spacing you see across different screens. New screen work should reach for
 * these instead of a fresh dp literal; existing screens are migrated incrementally, not in one
 * sweep, to keep each change reviewable.
 */
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
}
