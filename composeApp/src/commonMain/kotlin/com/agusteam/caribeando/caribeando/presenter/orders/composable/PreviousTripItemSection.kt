package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.old
import androidx.compose.material3.Text

/**
 * The handoff's "23. Viajes pasados" list -- rows 12dp apart (was a plain vertical stack with no
 * spacing). No longer maps `UpcomingOrders` -> `TripModel` via `.toDomain()` before rendering
 * (that mapper drops `providerName`/other fields the restyled `PreviousTripItem` needs); the raw
 * item now flows straight through.
 */
@Composable
fun PreviousTripItemSection(
    modifier: Modifier = Modifier.padding(top = 32.dp),
    oldItems: List<UpcomingOrders>,
    goDetails: (UpcomingOrders) -> Unit,
    rateOrderTrip: (UpcomingOrders) -> Unit
) {
    val colors = caribeandoColors
    Column(modifier) {
        Text(
            text = stringResource(Res.string.old),
            color = colors.textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (oldItems.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                oldItems.forEach { item ->
                    PreviousTripItem(
                        item = item,
                        goDetails = { goDetails(item) },
                        rateOrderTrip = { rateOrderTrip(it) })
                }
            }
        }
    }
}
