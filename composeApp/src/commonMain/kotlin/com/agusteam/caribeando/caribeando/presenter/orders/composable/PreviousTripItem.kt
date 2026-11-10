package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.rate_action

/**
 * The handoff's "23. Viajes pasados" row: 66x66 thumb radius12, title 600/13.5, "fecha ·
 * proveedor" 400/11. Unrated shows a red "Calificar" pill (600/11.5 white, radius999); rated
 * shows a small filled star + the numeric score (500/11 grey), no pill.
 *
 * 2026-08-20 (screens 18-30 fidelity pass): this row was missing the shared "TripRow (compact)"
 * component's own card chrome entirely -- "Card radius 16, 1dp #EDEFF1 border, 12dp padding" --
 * rendering as a bare, unbordered row instead. Added here (and to the other two TripRow call
 * sites, `WishItem.kt` and `TripProviderProfileScreen.kt`'s `ProviderTripRow`, the same gap in
 * all three). Thumb radius corrected 18dp (`Radii.cardShape`) -> the spec's 12dp for this
 * component specifically.
 *
 * 2026-08-20 (design handoff pass): [item] switched from the lossy `TripModel` (mapped via
 * `.toDomain()`, which drops `providerName`/`totalPayment`/etc.) to the raw `UpcomingOrders` --
 * the "· proveedor" half of the meta line and the rating callback's trip-header data (screen 24)
 * both need fields `toDomain()` never carried over.
 *
 * The rated state's numeric score isn't real: `UpcomingOrders` has no rating/score field at all
 * (only `hasBeenEvaluated: Boolean`) -- there's no backend data to show a specific number, so the
 * rated state shows a plain filled star with no number instead of fabricating one.
 */
@Composable
fun PreviousTripItem(
    item: UpcomingOrders,
    rateOrderTrip: (UpcomingOrders) -> Unit,
    goDetails: () -> Unit
) {
    val colors = caribeandoColors
    Row(
        Modifier
            .fillMaxWidth()
            .clip(Radii.panelShape)
            .border(1.dp, colors.panelBorder, Radii.panelShape)
            .bounceClick(scaleDown = 0.98f) { goDetails() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(66.dp).clip(RoundedCornerShape(12.dp)),
                model = item.tripImage,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column {
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = item.tripName,
                    color = colors.textPrimary,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                val meta = if (item.providerName.isNotBlank()) "${item.date} · ${item.providerName}" else item.date
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = meta,
                    color = colors.textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        if (!item.hasBeenEvaluated) {
            Box(
                // 2026-10-09 (design handoff): 48dp touch target around the 32dp pill
                // (tap handler outside minimumInteractiveComponentSize), spec padding 8 x 13.
                modifier = Modifier
                    .bounceClick(scaleDown = 0.95f) { rateOrderTrip(item) }
                    .minimumInteractiveComponentSize()
                    .background(colors.redFill, RoundedCornerShape(percent = 50))
                    .padding(horizontal = 13.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.rate_action),
                    color = androidx.compose.ui.graphics.Color.White, // white on red fill: same in both themes
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colors.star,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
