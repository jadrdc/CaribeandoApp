package com.agusteam.caribeando.presenter.wishlist.composable

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.formatDouble
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_favorite_filled

/**
 * The handoff's "26. Favoritos" TripRow: 88x88 thumb, rating line, price 600/14 red, and a
 * trailing 32x32 #FDF4F4 circle holding a filled heart 17 red that removes the item. Tapping the
 * row or thumb opens the trip.
 *
 * 2026-08-20 (screens 18-30 fidelity pass): added the shared "TripRow (compact)" component's own
 * card chrome -- "Card radius 16, 1dp #EDEFF1 border, 12dp padding" -- which this row never had
 * (same gap found on screens 23 and 29's TripRow usages).
 *
 * 2026-08-20 (design handoff pass, part 2): was a 150dp-wide vertical grid card with an
 * off-brand `secondary`/`grey500` palette and a plain provider-name row (no rating, no price, no
 * remove affordance) -- restyled to the spec's horizontal TripRow, matching the rating-line
 * pattern already established for "12. Detalles del viaje" (`ShoppingItemOverview.kt`: inlined
 * `Icons.Filled.Star` + `colors.star`, no bundled star drawable exists in this project).
 */
@Composable
fun WishItem(
    item: TripModel,
    onRemove: () -> Unit,
    goDetails: () -> Unit
) {
    val colors = caribeandoColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, colors.panelBorder, RoundedCornerShape(16.dp))
            .bounceClick(scaleDown = 0.98f) { goDetails() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier.size(88.dp).clip(RoundedCornerShape(12.dp)),
            model = item.images.firstOrNull() ?: "",
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Column(Modifier.weight(1f)) {
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = item.name,
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colors.star,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = formatDouble(item.rating),
                    color = colors.textPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " (${item.reviewCount} reseñas)",
                    color = colors.grey500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = formatMoney(item.price),
                color = colors.redFill,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(colors.redTint, CircleShape)
                .bounceClick(scaleDown = 0.85f) { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_favorite_filled),
                contentDescription = null,
                tint = colors.redFill,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}
