package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.caribeandoColors

@Composable
fun DateTripRowInformation(dataFrom: String, dateTo: String) {
    val colors = caribeandoColors
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Align items vertically
    ) {
        DateComponent("Fecha de salida", dataFrom)
        // 2026-08-19: this used to be TWO adjacent 1dp dividers (one gray, one red) -- a leftover
        // duplicate from color-testing, not an intentional double-line. Kept one, themed.
        // 2026-08-20 (dark-mode audit): "themed" above referred to picking one color, not
        // actually wiring a theme-reactive one -- `greyColor` is a raw, theme-blind val, so this
        // divider stayed light-grey (bright, out of place) against the dark page background
        // instead of the intended dark-mode-tuned divider color. Switched to `colors.rowDivider`.
        Box(
            Modifier
                .height(40.dp) // Adjust height as needed
                .width(1.dp)
                .background(colors.rowDivider)
        )
        DateComponent("Fecha de regreso", dateTo)
    }
}