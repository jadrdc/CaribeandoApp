package com.agusteam.caribeando.presenter.shopping.composable

import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.reviews_count
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.common.ReadMoreText
import com.agusteam.caribeando.presenter.formatDouble
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles

/**
 * The handoff's "12. Detalles del viaje" body opener: title 600/22/1.25, 8dp down `ic_star` 14
 * #FFB400 + rating 600/12.5 + "(N reseñas)" 400/12.5 #757575, 14dp down the description at
 * 400/13/1.65.
 *
 * 2026-08-20 (design handoff pass): the rating row previously used
 * `RatingAndReviews` (a Material star + English "(N reviews)" string, off-brand and
 * un-localized). Inlined a spec-matching row here instead -- there's no bundled `ic_star`
 * drawable in this project (confirmed: only `Icons.Filled.Star` was ever used for ratings), so
 * this keeps the Material star vector rather than inventing a resource that doesn't exist,
 * tinted to the spec's exact `colors.star` (#FFB400).
 */
@Composable
fun ShoppingItemOverview(
    title: String, description: String,
    rating: Double = 0.0, reviewCount: Int = 0,
    modifier: Modifier
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()

    Column(modifier) {
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 22.sp,
            lineHeight = 27.5.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = colors.star,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = formatDouble(rating),
                modifier = Modifier.padding(start = 4.dp),
                color = colors.textPrimary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = " " + stringResource(Res.string.reviews_count, reviewCount),
                color = colors.grey500,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Normal
            )
        }
        if (description.isNotEmpty()) {
            Box(Modifier.padding(top = 14.dp)) {
                ReadMoreText(text = description)
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}
