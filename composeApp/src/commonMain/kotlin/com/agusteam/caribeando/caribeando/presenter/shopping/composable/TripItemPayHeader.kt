package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.stripe.PaymentState
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.pay_trip

/**
 * The handoff's "14. Pagar viaje" header (spec screen 14): back button + "Pagar viaje" 600/17,
 * then a trip summary card -- 1dp #EDEFF1, radius 16, 12dp padding, 78x78 thumb radius 12,
 * title 600/14, meta and provider at 400/11.5.
 *
 * 2026-08-20 (design handoff pass): was a bare row (120x120 thumb, no card/border) with a
 * separate "Fecha" heading duplicating the leaving-time text already shown as meta. Restyled to
 * the spec's bordered summary card; the shared `NavigationBar` (already spec-restyled in the
 * Entrada pass) provides the "Pagar viaje" title.
 */
@Composable
fun TripItemPayHeader(
    state: PaymentState,
    onBackPressed: () -> Unit
) {
    val colors = caribeandoColors
    Column {
        NavigationBar(title = stringResource(Res.string.pay_trip)) { onBackPressed() }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .border(1.dp, colors.panelBorder, Radii.panelShape)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(78.dp).clip(RoundedCornerShape(12.dp)),
                model = state.profilePhoto,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column {
                Text(
                    text = state.title,
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "${state.destiny} · ${state.leavingTime}",
                    color = colors.textSecondary,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Normal
                )
                if (state.businessName.isNotEmpty()) {
                    Text(
                        text = state.businessName,
                        color = colors.textSecondary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
