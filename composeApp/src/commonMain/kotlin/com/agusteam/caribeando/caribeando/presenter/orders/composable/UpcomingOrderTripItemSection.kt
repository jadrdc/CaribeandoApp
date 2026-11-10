package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.domain.models.UpcomingOrders

/**
 * The handoff's "18. Próximos viajes" list -- a real vertical stack, cards 18dp apart.
 *
 * 2026-08-20 (design handoff pass): was a `HorizontalPager` (one trip visible at a time, swipe to
 * see more) with its own "Próximos" title baked in. The spec shows every upcoming trip as a
 * vertically stacked list of cards under a shared tab header, so the pager and this section's own
 * title are both dropped -- `OrderHistoryScreen` now owns the "Próximos viajes" / "Viajes
 * pasados" tab header for both sections.
 */
@Composable
fun UpcomingOrderTripItemSection(
    upcomingTripItemList: List<UpcomingOrders>, goDetails: (UpcomingOrders) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        upcomingTripItemList.forEach { item ->
            UpcomingTripItem(item = item, goDetails = { goDetails(item) })
        }
    }
}
