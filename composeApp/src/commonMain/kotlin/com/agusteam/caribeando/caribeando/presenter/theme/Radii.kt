package com.agusteam.caribeando.presenter.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * 2026-08-20 (design handoff pass): exact corner-radius scale from the
 * `design_handoff_caribeando_redesign` bundle ("Spacing, radii, sizing" section). The handoff
 * calls this "high fidelity... recreate precisely," and its 7 distinct radii don't map cleanly
 * onto Material3's 5-slot Shapes() (extraSmall/small/medium/large/extraLarge) used by the earlier
 * "modernize the UI" pass -- that pass's 28dp extraLarge card radius and pill-shaped CTA button
 * were reasonable guesses made without a real spec, and are both superseded by this file (card
 * radius is actually 18dp; the primary button is 14dp, not a pill). Prefer these named vals over
 * MaterialTheme.shapes.* wherever a component is directly implementing something the handoff
 * specifies, so the mapping to the spec stays legible at the call site.
 */
object Radii {
    /** Small control (chip inner elements, small icons-in-a-box). */
    val control = 12.dp

    /** Buttons and text inputs (PrimaryButton, SecondaryButton, Field). */
    val button = 14.dp

    /** Panels, list rows/groups, textareas. */
    val panel = 16.dp

    /** Cards, photos, modals. */
    val card = 18.dp

    /** Large tiles (EmptyState logo tile, success/failure icon tile). */
    val tile = 22.dp

    /** Bottom sheet top corners. */
    val sheet = 26.dp

    /** Pill / fully-round (chips, status pills, circular buttons -- use [pillShape] or CircleShape). */
    val pill = 999.dp

    val controlShape: Shape = RoundedCornerShape(control)
    val buttonShape: Shape = RoundedCornerShape(button)
    val panelShape: Shape = RoundedCornerShape(panel)
    val cardShape: Shape = RoundedCornerShape(card)
    val tileShape: Shape = RoundedCornerShape(tile)
    val sheetShape: Shape = RoundedCornerShape(topStart = sheet, topEnd = sheet, bottomStart = 0.dp, bottomEnd = 0.dp)
    val pillShape: Shape = RoundedCornerShape(percent = 50)
}
