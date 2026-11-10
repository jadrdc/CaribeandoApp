package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.shopping.model.ShoppingDetailModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource

/**
 * One cell of the handoff's two-column detail grid (spec screen 12): icon 19 #003049 + a label
 * 400/10.5 #5F6A70 over a value 500/12.5.
 *
 * 2026-08-20 (design handoff pass): was a horizontal icon+title+description row (title/desc
 * roles reversed vs. the spec, which treats `item.title` as the small caption label and
 * `item.description` as the prominent value). Restyled to the spec's stacked icon-over-label-
 * over-value cell; the 2-column grid layout itself is applied by the caller
 * (`ShoppingitemContent`), not here.
 */
@Composable
fun ShoppingItemDetail(item: ShoppingDetailModel) {
    val colors = caribeandoColors
    Column {
        Icon(
            tint = colors.textPrimary,
            painter = painterResource(item.icon),
            modifier = Modifier.size(19.dp),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = item.title,
            color = colors.textSecondary,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = item.description,
            color = colors.textPrimary,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
