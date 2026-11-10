package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors

/**
 * The handoff's "18. Próximos viajes" card: 1dp bordered radius18 card, 150dp photo with a date
 * badge inset 12 (white/95%, radius10, 600/11 navy), title 600/15/1.3, "proveedor" meta 400/11.5,
 * then a 1dp rule above a "Código" caps label / code row with the status (`timeUntilTrip`) in red
 * on the right.
 *
 * 2026-08-20 (design handoff pass): was a shadowed borderless card with a plain HorizontalPager
 * as its only host (see `UpcomingOrderTripItemSection`, now a vertical list instead) -- radius18
 * bordered card per spec, photo 200dp -> 150dp, added the confirmation-code/status footer row
 * (reusing `transactionId` as the display code, same value `ShoppingOrderDetailScreen`'s
 * "Detalles de la reserva" section already shows for this exact purpose).
 *
 * Known gap: the spec's meta line is "proveedor · N personas" -- `UpcomingOrders` has no
 * passenger-count field, so only the provider name is shown here rather than fabricating a
 * headcount.
 */
@Composable
fun UpcomingTripItem(
    item: UpcomingOrders, goDetails: () -> Unit
) {
    val colors = caribeandoColors
    val cardShape = Radii.cardShape
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .border(1.dp, colors.panelBorder, cardShape)
            .background(colors.pageBackground, cardShape)
            .bounceClick(scaleDown = 0.98f) { goDetails() }
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.height(150.dp).fillMaxWidth(),
                model = item.tripImage,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            if (item.date.isNotBlank())
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(colors.overlayOnPhoto, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = item.date,
                        color = colors.textPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
        }
        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(
                overflow = TextOverflow.Ellipsis,
                text = item.tripName,
                color = colors.textPrimary,
                fontSize = 15.sp,
                lineHeight = 19.5.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (item.providerName.isNotBlank())
                Text(
                    modifier = Modifier.padding(top = 3.dp),
                    text = item.providerName,
                    color = colors.textSecondary,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Normal
                )
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .background(colors.hairline)
                    .height(1.dp)
            )
            Row(
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CÓDIGO",
                        color = colors.textSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = item.transactionId,
                        color = colors.textPrimary,
                        fontSize = 13.sp,
                        letterSpacing = 0.05.em,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (item.timeUntilTrip.isNotBlank())
                    Text(
                        text = item.timeUntilTrip,
                        color = colors.redFill,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
            }
        }
    }
}
