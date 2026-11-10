package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles

/**
 * The app's primary CTA button (the handoff's "PrimaryButton"), used across nearly every screen
 * (login, signup, checkout, cancellation, review, report-issue, filters, ...).
 *
 * 2026-08-19: fixed a real bug -- the caller-supplied `modifier` (almost always
 * Modifier.fillMaxWidth() or fillMaxWidth(fraction)) was applied to the inner Text instead of the
 * clickable Box. `modifier` now applies to the Box, where the caller expects it to.
 *
 * 2026-08-20 (design handoff pass): re-specced exactly from the handoff's "PrimaryButton" entry --
 * "Full width, #D62828 fill, radius 14, 15dp padding, label 600/15 white, centered. Disabled: fill
 * #EDB4B4, same label." This SUPERSEDES the 2026-08-19 "modernize the UI" pass, which (without a
 * real spec to work from) guessed a pill shape (28dp radius) and a tinted drop shadow -- neither
 * is in the handoff, so both are removed here in favor of the exact spec. The press-scale
 * (`bounceClick`, added in the interactivity pass) is kept since it's additive behavior, not a
 * visual spec the handoff defines one way or the other.
 */
@Composable
fun ActionButton(
    isValid: Boolean = true,
    modifier: Modifier = Modifier,
    text: String, onClick: () -> Unit
) {
    val colors = caribeandoColors
    Box(
        // contentAlignment centers the Text regardless of what width modifier the caller passes
        // in (fillMaxWidth(), fillMaxWidth(0.6f), or nothing) -- previously the Text itself
        // carried fillMaxWidth() + textAlign=Center to achieve this; now that `modifier` (the
        // actual sizing) lives on the Box where it belongs, the Box needs to do the centering.
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                if (isValid) colors.redFill else colors.disabledButton,
                Radii.buttonShape
            )
            .bounceClick(enabled = isValid, scaleDown = 0.97f) { onClick() }
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(15.dp),
            text = text,
            color = Color.White,
            style = caribeandoTextStyles().primaryButtonLabel,
        )
    }
}
