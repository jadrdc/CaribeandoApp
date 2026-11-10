package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles

/**
 * The handoff's "SecondaryButton": "Full width, transparent fill, 1dp #E2E2E2 border, radius 14,
 * 14dp padding, label 600/14 in #003049." Used for "Continue with Google," "Cambiar método de
 * pago," "Seguir explorando," etc.
 */
@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val colors = caribeandoColors
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(1.dp, colors.panelFieldBorder, Radii.buttonShape)
            .bounceClick(enabled = enabled, scaleDown = 0.97f) { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(14.dp),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            style = caribeandoTextStyles().primaryButtonLabel.copy(fontSize = 14.sp),
        )
    }
}
