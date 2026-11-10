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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "Pagar viaje" payment-option card (spec screen 14).
 * Selected: 1.6dp #D62828 border, #FDF4F4 fill, radius 16, 15dp padding; title 600/13.5 with a
 * 22x22 radio right (border-box, 6dp red ring, fully round); amount 600/20 red; description
 * 400/11.5/1.5 grey.
 * Unselected: 1dp #E2E2E2 border, no fill, title 500/13.5, 1.5dp #CFD5DA ring, amount 600/17
 * navy, no description.
 *
 * 2026-08-20 (design handoff pass): was a generic `RadioButton` + text row (16sp title/desc for
 * both states, no amount shown, no selected-state fill/border treatment). Both call sites
 * (`ShoppingPaymentWay`'s booking/total options) now pass their formatted amount and
 * (for the selected option) a description.
 */
@Composable
fun CustomRadioButton(
    title: String,
    description: String,
    amount: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = caribeandoColors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) colors.redTint else Color.Transparent, Radii.panelShape)
            .border(
                if (isSelected) 1.6.dp else 1.dp,
                if (isSelected) colors.redFill else colors.panelFieldBorder,
                Radii.panelShape
            )
            .bounceClick(scaleDown = 0.98f) { onClick() }
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 13.5.sp,
                color = colors.textPrimary
            )
            Box(
                modifier = Modifier.size(22.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(6.dp, colors.redFill, CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(1.5.dp, colors.dashedBorder, CircleShape)
                    )
                }
            }
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = amount,
            fontWeight = FontWeight.SemiBold,
            fontSize = if (isSelected) 20.sp else 17.sp,
            color = if (isSelected) colors.redFill else colors.textPrimary
        )
        if (isSelected && description.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = description,
                fontWeight = FontWeight.Normal,
                fontSize = 11.5.sp,
                lineHeight = 17.25.sp,
                color = colors.textSecondary
            )
        }
    }
}
