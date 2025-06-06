package com.agusteam.caribeando.presenter.orders.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.presenter.theme.CustomFontFamily
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.pending

@Composable
fun UpcomingTripItem(
    item: TripModel, goDetails: () -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 8.dp, // Adjust elevation as needed
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f), // Soft shadow color
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { goDetails() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(Modifier) {
                AsyncImage(
                    modifier = Modifier.height(180.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    model = item.images.firstOrNull() ?: "",
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                Box(Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(Res.string.pending),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.TopStart).background(
                            Color.White, RoundedCornerShape(8.dp)
                        ).padding(8.dp)
                    )
                }
            }
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier,
                    text = item.name,
                    color = secondary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold

                )
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
        }
    }
}