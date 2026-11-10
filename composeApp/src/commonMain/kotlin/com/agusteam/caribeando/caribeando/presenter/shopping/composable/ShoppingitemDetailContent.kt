package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.shopping.model.ShoppingDetailModel

/**
 * The handoff's "12. Detalles del viaje" two-column detail grid, 16dp gaps -- Destino, Lugar de
 * salida, Hora de salida, Hora de llegada, Pago de reserva, Pago total (2 per row, 3 rows).
 *
 * 2026-08-20 (design handoff pass): was a single vertical list of icon+text rows; now chunked
 * into pairs and laid out as a real 2-column grid, each cell taking equal weight.
 */
@Composable
fun ShoppingitemContent(itemsDetails: List<ShoppingDetailModel>, modifier: Modifier) {
    Column(modifier) {
        itemsDetails.chunked(2).forEachIndexed { index, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (index == 0) 0.dp else 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { item ->
                    Column(Modifier.weight(1f)) {
                        ShoppingItemDetail(item)
                    }
                }
                if (rowItems.size == 1) {
                    Column(Modifier.weight(1f)) {}
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}
