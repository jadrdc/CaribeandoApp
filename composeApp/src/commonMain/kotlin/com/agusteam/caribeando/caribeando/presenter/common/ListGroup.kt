package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.chevron_right_small

/**
 * The handoff's "ListGroup": "Rounded container, radius 16, 1dp #EDEFF1 border, clipToBounds.
 * Rows are 14-15dp padding; between rows a 1dp #F2F4F5 divider, full bleed inside the container.
 * Label-value row: label 400/12 #5F6A70 left, value 500/13 #003049 right. Navigation row: leading
 * icon 19 #003049, label 500/13, trailing chevron_right_small 16 in #8A9298."
 *
 * Used across the handoff for "Información de pago," "Datos del viaje," Perfil's field/navigation
 * groups, and more. Pass [rows] as a list of [ListGroupLabelValueRow] / [ListGroupNavigationRow]
 * (or a custom row composable) -- this container only owns the chrome (border/clip) and the
 * inter-row dividers.
 */
@Composable
fun ListGroup(
    modifier: Modifier = Modifier,
    rows: List<@Composable () -> Unit>,
) {
    val colors = caribeandoColors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(Radii.panelShape)
            .border(1.dp, colors.panelBorder, Radii.panelShape)
    ) {
        rows.forEachIndexed { index, row ->
            row()
            if (index != rows.lastIndex) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(colors.rowDivider)
                        .padding(vertical = 0.5.dp)
                )
            }
        }
    }
}

@Composable
fun ListGroupLabelValueRow(label: String, value: String) {
    val colors = caribeandoColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Normal)
        Text(text = value, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

/** Trailing chevron_right_small is always shown per spec; use [ListGroupLabelValueRow] instead when a row has no navigation affordance. */
@Composable
fun ListGroupNavigationRow(
    icon: Painter,
    label: String,
    onClick: () -> Unit,
) {
    val colors = caribeandoColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f) { onClick() }
            .padding(horizontal = 15.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = icon, contentDescription = null, tint = colors.textPrimary, modifier = Modifier.size(19.dp))
        Text(
            text = label,
            color = colors.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(Res.drawable.chevron_right_small),
            contentDescription = null,
            tint = colors.iconInactive,
            modifier = Modifier.size(16.dp)
        )
    }
}
