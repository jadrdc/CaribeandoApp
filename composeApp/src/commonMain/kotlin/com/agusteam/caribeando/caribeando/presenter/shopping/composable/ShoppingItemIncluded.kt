package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "Incluye" block (spec screen 12): rows of a 5x5 `#D62828` dot and 400/12.5
 * `#3F4A52` text, 9dp apart.
 *
 * 2026-08-20 (design handoff pass): was a checkmark-icon list (`Icons.Rounded.Check`). Restyled
 * to the spec's dot-and-text row.
 */
@Composable
fun ShoppingItemIncluded(
    title: String,
    items: List<String>,
    modifier: Modifier,
) {
    val colors = caribeandoColors
    Column(modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        items.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = if (index == 0) 12.dp else 9.dp)
            ) {
                Box(
                    Modifier
                        .size(5.dp)
                        .background(colors.redFill, CircleShape)
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = item,
                    color = colors.textBody,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
