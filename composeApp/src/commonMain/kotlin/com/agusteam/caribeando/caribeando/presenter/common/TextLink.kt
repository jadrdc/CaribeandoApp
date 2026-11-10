package com.agusteam.caribeando.presenter.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "TextLink": "Centered label 600/13 in #D62828. The muted variant is 500/12.5 in
 * #5F6A70 with underline (used for destructive secondary actions like Cancelar reserva)."
 *
 * Plain `Text` + `bounceClick` rather than `ClickableText` -- there's no need for per-character
 * span click detection here (just "tap this whole label"), and layering `bounceClick`'s
 * `Modifier.clickable` on top of `ClickableText`'s own internal tap-gesture detector risks the two
 * pointer-input handlers fighting over the same gesture (double firing, or the inner one never
 * seeing the event).
 */
@Composable
fun TextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    muted: Boolean = false,
    // 2026-08-20 (screens 1-17 fidelity pass): a couple of the handoff's screens ask for a
    // TextLink in a color/underline combo that neither hardcoded variant covers -- screen 3's
    // "Volver a iniciar sesion" wants 500/12.5 in #003049 (muted's size/weight, but navy not
    // grey), and screen 7's "Borrar filtros" wants 600/13 in #003049, underlined (default's
    // size/weight, navy, plus underline). Rather than inventing a third boolean flag per
    // combination, these two optional overrides let a call site correct just the color/underline
    // while still inheriting size+weight from `muted` -- every existing call site keeps its exact
    // prior appearance since both default to null.
    color: Color? = null,
    underline: Boolean? = null,
    // 2026-08-20 (screens 18-30 fidelity pass): screen 21's "Mantener reserva" wants "600/14
    // #003049, no fill" -- a size neither hardcoded variant provides -- so this follows the same
    // null-default override pattern as `color`/`underline` above rather than adding a fourth
    // hardcoded variant or a one-off `Text` at that single call site.
    fontSize: TextUnit? = null,
) {
    val colors = caribeandoColors
    Text(
        text = text,
        modifier = modifier.bounceClick(scaleDown = 0.97f, indication = null) { onClick() },
        color = color ?: if (muted) colors.textSecondary else colors.redText,
        fontSize = fontSize ?: if (muted) 12.5.sp else 13.sp,
        fontWeight = if (muted) FontWeight.Medium else FontWeight.SemiBold,
        textDecoration = if (underline ?: muted) TextDecoration.Underline else TextDecoration.None,
        textAlign = TextAlign.Center,
    )
}
