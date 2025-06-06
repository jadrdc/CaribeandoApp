package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.caribeando.presenter.common.PendingReview
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.theme.CustomFontFamily
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun PreviousTripItem(
    item: TripModel,
    rateOrderTrip: (String) -> Unit,
    goDetails: () -> Unit
) {

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp).clickable { goDetails() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)),
                model = item.images.firstOrNull() ?: "",
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column {
                Text(
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier,
                    text = item.name,
                    color = secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (item.price > 0)
                    Text(
                        text = "$${item.price}",
                        color = primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = CustomFontFamily(),
                        fontSize = 14.sp
                    )
                Text(
                    modifier = Modifier,
                    text = item.date,
                    color = grey500,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        if (!item.hasBeenEvaluated) {
            PendingReview {
                rateOrderTrip(item.transactionId)
            }
        }
    }
}