package com.agusteam.caribeando.presenter.theme

import androidx.compose.ui.graphics.Color

// 2026-08-19: kept as the single source of truth for the brand palette, but these are no longer
// the only place color lives in the app. They now also feed CaribeandoColorScheme (Theme.kt),
// which is what actually gets wired into MaterialTheme at the app root (MainNavigationFlow). Any
// new UI should prefer MaterialTheme.colorScheme.* over these raw vals so it responds to theme
// changes (e.g. a future dark theme) automatically; these vals stay for the ~50 existing call
// sites that already reference them directly, so nothing breaks in this pass.
val blackColor = Color(0xFF000000)
val grey500 = Color(0xFF757575)
val primary = Color(0xFFD62828)
val secondary = Color(0xFF003049)
val greyColor = Color(0xFFE2E2E2)
val backGround = Color.White

// Supporting tones added 2026-08-19 alongside CaribeandoColorScheme — derived from the existing
// primary/secondary brand colors, not new brand decisions.
val primaryContainer = Color(0xFFF9D9D6)   // light tint of primary, for chips/badges/highlighted cards
val onPrimaryContainer = Color(0xFF410002) // dark-on-light-tint text/icon color, WCAG-safe over primaryContainer
val errorColor = Color(0xFFB3261E)         // distinct from brand `primary` red, used for real error/destructive states (e.g. cancel order)
val onErrorColor = Color.White
val surfaceVariant = greyColor
val onSurfaceVariant = grey500
val outline = Color(0xFFC7C7C7)
