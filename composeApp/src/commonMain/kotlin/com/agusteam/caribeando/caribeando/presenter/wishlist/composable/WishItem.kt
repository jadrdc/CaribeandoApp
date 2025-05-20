package com.agusteam.caribeando.presenter.wishlist.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun WishItem(
    item: TripModel, goDetails: () -> Unit
) {

    Column(
        modifier = Modifier.width(150.dp).clickable {
            goDetails()
        }) {
        AsyncImage(
            modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)),
            model = item.images.firstOrNull() ?: "",
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        Text(
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp).width(150.dp),
            text = item.name,
            color = secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = item.destiny,
            color = grey500,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier.size(24.dp).clip(CircleShape),
                model = item.businessImage,
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            Text(
                text = item.businessName,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}