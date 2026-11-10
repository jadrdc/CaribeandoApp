package com.agusteam.caribeando.presenter.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/**
 * 2026-08-19: added as part of the UI consistency pass. Before this, the app had no single
 * MaterialTheme wrapper at its root (MainNavigationFlow) at all -- login/signup/booking screens
 * rendered with Compose's bare defaults (default font, default purple ColorScheme), and only
 * HomeScreen applied MaterialTheme(typography = CustomTypography()), without ever setting a
 * colorScheme. The brand colors only existed as raw Color vals referenced directly by ~50 files.
 *
 * CaribeandoLightColorScheme maps the brand palette onto Material3's semantic color roles, so any
 * component that reads MaterialTheme.colorScheme (buttons, TopAppBar, etc.) gets consistent brand
 * colors everywhere, not just call sites that happened to import the raw vals. This is additive:
 * existing files that reference `primary`/`secondary`/`backGround` directly keep compiling and
 * looking the same, since those vals are unchanged.
 *
 * 2026-08-20 (design handoff pass): added CaribeandoDarkColorScheme, sourced from the handoff's
 * "Color — dark" table. Existing call sites that read raw Colors.kt vals directly (the ~50 from
 * before) are NOT theme-reactive and will stay light-only until each screen is restyled to read
 * `MaterialTheme.colorScheme.*` / `caribeandoColors` instead -- that restyle happens screen by
 * screen as part of implementing the handoff, not in this token-foundation pass.
 */
val CaribeandoLightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = Color.White,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    secondary = secondary,
    onSecondary = Color.White,
    background = backGround,
    onBackground = blackColor,
    surface = backGround,
    onSurface = blackColor,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    outline = outline,
    error = errorColor,
    onError = onErrorColor,
)

val CaribeandoDarkColorScheme = darkColorScheme(
    primary = DarkCaribeandoColors.redFill,
    onPrimary = Color.White,
    primaryContainer = DarkCaribeandoColors.redTint,
    onPrimaryContainer = DarkCaribeandoColors.redText,
    secondary = DarkCaribeandoColors.navyFill,
    onSecondary = DarkCaribeandoColors.textPrimary,
    background = DarkCaribeandoColors.pageBackground,
    onBackground = DarkCaribeandoColors.textPrimary,
    surface = DarkCaribeandoColors.pageBackground,
    onSurface = DarkCaribeandoColors.textPrimary,
    surfaceVariant = DarkCaribeandoColors.fieldFill,
    onSurfaceVariant = DarkCaribeandoColors.textSecondary,
    outline = DarkCaribeandoColors.panelFieldBorder,
    error = DarkCaribeandoColors.redText,
    onError = Color.White,
)

/**
 * 2026-08-19: added for the "modernize the UI" pass, sourced from Material 3's own shape token
 * scale. 2026-08-20 (design handoff pass): re-pointed at the handoff's exact radii instead of the
 * earlier researched-but-unverified guesses -- e.g. the earlier pass made cards a very round 28dp
 * and the CTA button a full pill; the handoff specifies cards at 18dp and the CTA button at a
 * plain 14dp radius, not a pill. See `Radii.kt` for the full 7-value scale this is drawn from
 * (Material3's Shapes() only has 5 slots, so this is a lossy approximation for pure-M3 components;
 * anything directly implementing a handoff spec should reach for `Radii.*` directly instead of
 * these slots).
 */
val CaribeandoShapes = Shapes(
    extraSmall = RoundedCornerShape(Radii.control), // 12dp
    small = RoundedCornerShape(Radii.button),        // 14dp
    medium = RoundedCornerShape(Radii.panel),        // 16dp
    large = RoundedCornerShape(Radii.card),          // 18dp
    extraLarge = RoundedCornerShape(Radii.tile),     // 22dp
)

/**
 * Single theming entry point for the whole app. Wrap the app root (MainNavigationFlow) in this
 * instead of the ad hoc per-screen MaterialTheme(typography = ...) call that used to live only in
 * HomeScreen.
 *
 * 2026-08-20 (design handoff pass): now theme-aware. `darkTheme` defaults to the system setting
 * (`isSystemInDarkTheme()`), matching the handoff's "each color carries a light and a dark value,
 * switched by a theme prop." Provides both the Material3 ColorScheme (light/dark) AND the extended
 * color roles (`caribeandoColors`) that don't have a Material3 slot -- read the latter for any
 * role not in Material3's fixed set (field fill, hairline, shimmer, scrim, etc).
 */
@Composable
fun CaribeandoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) DarkCaribeandoColors else LightCaribeandoColors
    CompositionLocalProvider(LocalCaribeandoColors provides extendedColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) CaribeandoDarkColorScheme else CaribeandoLightColorScheme,
            typography = CustomTypography(),
            shapes = CaribeandoShapes,
            content = content
        )
    }
}
