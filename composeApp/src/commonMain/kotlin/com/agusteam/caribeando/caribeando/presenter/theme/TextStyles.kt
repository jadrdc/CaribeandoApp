package com.agusteam.caribeando.presenter.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * 2026-08-20 (design handoff pass): the named type roles from the handoff's "Typography" table.
 * Material3's fixed 15-slot `Typography` (displayLarge, headlineMedium, ...) doesn't have a slot
 * per named role this design system uses (there's no "balance hero amount" or "eyebrow" concept
 * in Material3), so this is a parallel catalog instead of trying to force ~28 roles into 15 M3
 * slots. All roles use `CustomFontFamily()` (Poppins), matching `CustomTypography()`.
 *
 * `fieldLabelCaps` and `eyebrowCaps` are specced uppercase with letter-spacing -- apply
 * `.uppercase()` to the string at the call site; TextStyle has no text-transform.
 *
 * A few roles in the handoff are given as a range (e.g. "21–22", "11–11.5") rather than one
 * number; this picks one representative value from that range rather than plumbing a second axis
 * through the whole app for a sub-1sp difference.
 */
data class CaribeandoTextStyles(
    val screenTitleTabRoot: TextStyle,
    val screenTitlePushed: TextStyle,
    val screenTitleCompact: TextStyle,
    val successFailureHeadline: TextStyle,
    val balanceHeroAmount: TextStyle,
    val navBarTitle: TextStyle,
    val sectionHeader: TextStyle,
    val feedCardTitle: TextStyle,
    val listRowTitle: TextStyle,
    val priceFeed: TextStyle,
    val priceCheckoutTotal: TextStyle,
    val bodyDescription: TextStyle,
    val metadata: TextStyle,
    val metadataSmall: TextStyle,
    val fieldLabelCaps: TextStyle,
    val eyebrowCaps: TextStyle,
    val primaryButtonLabel: TextStyle,
    val sheetButtonLabel: TextStyle,
    val pillButtonLabel: TextStyle,
    val chipLabel: TextStyle,
    val tabActive: TextStyle,
    val tabInactive: TextStyle,
    val bottomNavLabelActive: TextStyle,
    val bottomNavLabelInactive: TextStyle,
    val ratingValue: TextStyle,
)

@Composable
fun caribeandoTextStyles(): CaribeandoTextStyles {
    val fontFamily = CustomFontFamily()
    return CaribeandoTextStyles(
        screenTitleTabRoot = TextStyle(fontFamily = fontFamily, fontSize = 26.sp, lineHeight = 30.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.02).em),
        screenTitlePushed = TextStyle(fontFamily = fontFamily, fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.02).em),
        screenTitleCompact = TextStyle(fontFamily = fontFamily, fontSize = 22.sp, lineHeight = 27.5.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.02).em),
        successFailureHeadline = TextStyle(fontFamily = fontFamily, fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.02).em),
        balanceHeroAmount = TextStyle(fontFamily = fontFamily, fontSize = 34.sp, lineHeight = 37.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.02).em),
        navBarTitle = TextStyle(fontFamily = fontFamily, fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
        sectionHeader = TextStyle(fontFamily = fontFamily, fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
        feedCardTitle = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, lineHeight = 20.8.sp, fontWeight = FontWeight.SemiBold),
        listRowTitle = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, lineHeight = 18.2.sp, fontWeight = FontWeight.SemiBold),
        priceFeed = TextStyle(fontFamily = fontFamily, fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
        priceCheckoutTotal = TextStyle(fontFamily = fontFamily, fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
        bodyDescription = TextStyle(fontFamily = fontFamily, fontSize = 13.sp, lineHeight = 21.45.sp, fontWeight = FontWeight.Normal),
        metadata = TextStyle(fontFamily = fontFamily, fontSize = 12.5.sp, fontWeight = FontWeight.Normal),
        metadataSmall = TextStyle(fontFamily = fontFamily, fontSize = 11.25.sp, fontWeight = FontWeight.Normal),
        fieldLabelCaps = TextStyle(fontFamily = fontFamily, fontSize = 10.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.10.em),
        eyebrowCaps = TextStyle(fontFamily = fontFamily, fontSize = 9.5.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.16.em),
        primaryButtonLabel = TextStyle(fontFamily = fontFamily, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center),
        sheetButtonLabel = TextStyle(fontFamily = fontFamily, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center),
        pillButtonLabel = TextStyle(fontFamily = fontFamily, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center),
        chipLabel = TextStyle(fontFamily = fontFamily, fontSize = 12.5.sp, fontWeight = FontWeight.Medium),
        tabActive = TextStyle(fontFamily = fontFamily, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold),
        tabInactive = TextStyle(fontFamily = fontFamily, fontSize = 13.5.sp, fontWeight = FontWeight.Normal),
        bottomNavLabelActive = TextStyle(fontFamily = fontFamily, fontSize = 10.sp, fontWeight = FontWeight.Medium),
        bottomNavLabelInactive = TextStyle(fontFamily = fontFamily, fontSize = 10.sp, fontWeight = FontWeight.Normal),
        ratingValue = TextStyle(fontFamily = fontFamily, fontSize = 12.25.sp, fontWeight = FontWeight.SemiBold),
    )
}
