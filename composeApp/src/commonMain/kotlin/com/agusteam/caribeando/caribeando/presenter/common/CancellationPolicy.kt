package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "Políticas de cancelación" panel (spec screen 12): `#F4F5F6` panel radius 16,
 * 15dp padding, title 600/13.5, body 400/12/1.6.
 *
 * 2026-08-20 (design handoff pass): was a plain row with a trailing chevron and a bottom
 * divider, implying it opened something -- `onClick` was always passed an empty `{}` lambda at
 * the one call site, a real dead-click. Restyled to the spec's static info panel (no chevron, no
 * click affordance) since there's nowhere for it to navigate; `onClick` param kept for source
 * compatibility but no longer wired to a visible affordance.
 */
@Composable
fun CancellationPolicy(
    modifier: Modifier,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val colors = caribeandoColors
    Column(
        modifier
            .fillMaxWidth()
            .background(colors.fieldFill, Radii.panelShape)
            .padding(15.dp)
    ) {
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = description,
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 19.2.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
