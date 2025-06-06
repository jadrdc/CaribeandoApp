package com.agusteam.caribeando.presenter.explore.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.caribeando.presenter.common.RatingAndReviews
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.common.FavoriteButton
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.theme.CustomFontFamily
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun TripItem(
    item: TripModel,
    onClick: () -> Unit,
    toggleFavorite: (ExploreEvent) -> Unit
) {
    Column(Modifier.clickable { onClick() }) {
        Box {
            AsyncImage(
                modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp)),
                model = item.images.firstOrNull() ?: "",
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Box(
                Modifier.align(
                    Alignment.TopEnd
                ).padding(16.dp)
            ) {
                FavoriteButton(isSavedForLater = item.isSavedForLater) {
                    toggleFavorite(
                        ExploreEvent.OnShoppingItemMarked(item)
                    )
                }
            }
        }
        Column(Modifier) {
            Row(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    color = secondary,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(item.categoryList) { category ->
                        AsyncImage(
                            model = category.image,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(32.dp),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(primary)
                        )
                    }
                }
            }
            RatingAndReviews(averageRating = item.rating, reviewCount = item.reviewCount.toLong())
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.date,
                    color = grey500,
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyMedium // Use the appropriate typography style

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
            Text(
                text = formatMoney(item.price.toInt()),
                color = secondary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier,
                fontWeight = FontWeight.SemiBold,
                fontFamily = CustomFontFamily(),
                fontSize = 14.sp
            )
        }
    }
}