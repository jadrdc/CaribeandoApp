package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.domain.models.UpcomingOrders
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.old
import com.agusteam.caribeando.caribeando.presenter.common.EmptyState
import com.agusteam.caribeando.data.mappers.toDomain

@Composable
fun PreviousTripItemSection(
    modifier: Modifier = Modifier.padding(top = 32.dp),
    oldItems: List<UpcomingOrders>, goDetails: (UpcomingOrders) -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(Res.string.old),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (oldItems.isNotEmpty()) {
            oldItems.forEach { item ->
                PreviousTripItem(item.toDomain()) { goDetails(item) }
            }
        } else {
            EmptyState(
                message = "No hay viajes que mostrar",
                actionText = "Intente nuevamente"
            )
        }
    }
}