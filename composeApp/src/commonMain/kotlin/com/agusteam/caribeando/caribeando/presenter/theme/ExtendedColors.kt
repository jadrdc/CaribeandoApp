package com.agusteam.caribeando.presenter.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 2026-08-20 (design handoff pass): the full color-role system from the
 * `design_handoff_caribeando_redesign` bundle's "Design tokens" section, light AND dark.
 *
 * `Colors.kt` / `CaribeandoColorScheme` (Theme.kt) only cover Material3's fixed semantic slots
 * (primary, background, surface, ...), which isn't enough roles for this design system --
 * things like a dashed-border color, a shimmer gradient pair, or "navy as a fill vs navy as
 * text" (two different colors in dark mode) have no Material3 slot to live in. This class holds
 * those as their own named roles instead of overloading unrelated M3 slots.
 *
 * Read via `caribeandoColors()` inside any composable, the same way you'd read
 * `MaterialTheme.colorScheme`. `CaribeandoTheme` (Theme.kt) provides the right instance
 * (Light/Dark) based on `isSystemInDarkTheme()`.
 *
 * Three deliberate splits worth knowing before using these (see the handoff's "Three decisions"
 * note):
 * 1. Red splits by role: [redFill] is the brand red unchanged in both themes (buttons, active
 *    nav dot); [redText] is a lighter red (#FF6E6E) used ONLY as text/icon color in dark mode,
 *    because the brand red fails WCAG AA as body text on a dark ground.
 * 2. Navy splits by role: [textPrimary] is navy-as-text (inverts to near-white in dark);
 *    [navyFill] is navy-as-a-filled-surface (active chip/filter button), which lifts to a
 *    lighter navy in dark mode so it still reads as a filled surface against the dark background.
 * 3. [cream] never changes -- `logo.png` is a raster with its own cream background baked in.
 */
@Immutable
data class CaribeandoExtendedColors(
    val pageBackground: Color,
    val textPrimary: Color,
    val navyFill: Color,
    val redFill: Color,
    val redText: Color,
    val textSecondary: Color,
    val textBody: Color,
    val grey500: Color,
    val iconInactive: Color,
    val fieldFill: Color,
    val hairline: Color,
    val panelBorder: Color,
    val rowDivider: Color,
    val redTint: Color,
    val cream: Color,
    val dashedBorder: Color,
    val disabledText: Color,
    val subtleFill: Color,
    val avatarPlaceholder: Color,
    val emptyStar: Color,
    val dividerFill: Color,
    val panelFieldBorder: Color,
    val disabledButton: Color,
    val sheetGrabber: Color,
    val spinnerTrack: Color,
    val scrim: Color,
    val overlayOnPhoto: Color,
    val stickyBar: Color,
    val photoPlaceholderStart: Color,
    val photoPlaceholderEnd: Color,
    val shimmerBase: Color,
    val shimmerHighlight: Color,
    val mapWater: Color,
    val mapGrid: Color,
    val mapLand: Color,
    val star: Color,
)

val LightCaribeandoColors = CaribeandoExtendedColors(
    pageBackground = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF003049),
    navyFill = Color(0xFF003049),
    redFill = Color(0xFFD62828),
    redText = Color(0xFFD62828),
    textSecondary = Color(0xFF5F6A70),
    textBody = Color(0xFF3F4A52),
    grey500 = Color(0xFF757575),
    iconInactive = Color(0xFF8A9298),
    fieldFill = Color(0xFFF4F5F6),
    hairline = Color(0xFFEEF0F1),
    panelBorder = Color(0xFFEDEFF1),
    rowDivider = Color(0xFFF2F4F5),
    redTint = Color(0xFFFDF4F4),
    cream = Color(0xFFF6EDE2),
    dashedBorder = Color(0xFFCFD5DA),
    disabledText = Color(0xFFCFD5DA),
    subtleFill = Color(0xFFFBFBFC),
    avatarPlaceholder = Color(0xFFE7EBEE),
    emptyStar = Color(0xFFE2E2E2),
    dividerFill = Color(0xFFE2E2E2),
    panelFieldBorder = Color(0xFFE2E2E2),
    disabledButton = Color(0xFFEDB4B4),
    sheetGrabber = Color(0xFFDDE2E6),
    spinnerTrack = Color(0xFFF0DCDC),
    scrim = Color(0x57003049), // rgba(0,48,73,.34)
    overlayOnPhoto = Color(0xF0FFFFFF), // rgba(255,255,255,.94)
    stickyBar = Color(0xF7FFFFFF), // rgba(255,255,255,.97)
    photoPlaceholderStart = Color(0xFFE9EDF0),
    photoPlaceholderEnd = Color(0xFFDCE3E8),
    shimmerBase = Color(0xFFF1F3F4),
    shimmerHighlight = Color(0xFFE6E9EB),
    mapWater = Color(0xFFE8EEF1),
    mapGrid = Color(0xFFDBE4E9),
    mapLand = Color(0xFFD7E1E7),
    star = Color(0xFFFFB400),
)

val DarkCaribeandoColors = CaribeandoExtendedColors(
    pageBackground = Color(0xFF101E28),
    textPrimary = Color(0xFFE8F1F6),
    navyFill = Color(0xFF3E6479),
    redFill = Color(0xFFD62828),
    redText = Color(0xFFFF6E6E),
    textSecondary = Color(0xFF9FB2BE),
    textBody = Color(0xFFC7D6DF),
    grey500 = Color(0xFF8FA1AC),
    iconInactive = Color(0xFF7E9099),
    fieldFill = Color(0xFF182934),
    hairline = Color(0xFF1E3039),
    panelBorder = Color(0xFF22353F),
    rowDivider = Color(0xFF1C2E38),
    redTint = Color(0xFF2A1618),
    cream = Color(0xFFF6EDE2), // unchanged -- logo.png has a baked-in cream background
    dashedBorder = Color(0xFF3A4F5C),
    disabledText = Color(0xFF5A6E7A),
    subtleFill = Color(0xFF0D1A23),
    avatarPlaceholder = Color(0xFF223440),
    emptyStar = Color(0xFF2C4050),
    dividerFill = Color(0xFF22343F),
    panelFieldBorder = Color(0xFF263B47),
    disabledButton = Color(0xFF5E2A2A),
    sheetGrabber = Color(0xFF344854),
    spinnerTrack = Color(0xFF3A2426),
    scrim = Color(0x9E000000), // rgba(0,0,0,.62)
    overlayOnPhoto = Color(0xE00C1A22), // rgba(12,26,34,.88)
    stickyBar = Color(0xF7101E28), // rgba(16,30,40,.97)
    photoPlaceholderStart = Color(0xFF1B2B35),
    photoPlaceholderEnd = Color(0xFF16232C),
    shimmerBase = Color(0xFF17262F),
    shimmerHighlight = Color(0xFF1E303B),
    mapWater = Color(0xFF132430),
    mapGrid = Color(0xFF1B2E3A),
    mapLand = Color(0xFF1E3441),
    star = Color(0xFFFFB400), // unchanged
)

val LocalCaribeandoColors = staticCompositionLocalOf { LightCaribeandoColors }

/** Reads the current theme's extended color roles -- use the same way as `MaterialTheme.colorScheme`. */
val caribeandoColors: CaribeandoExtendedColors
    @androidx.compose.runtime.Composable
    get() = LocalCaribeandoColors.current
