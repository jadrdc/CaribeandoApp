package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.presenter.getTimePeriod
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.secondary
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.agency

@Composable
fun ItemProviderOverviewItem(
    modifier: Modifier = Modifier,
    state: ItemProviderState,
    goProviderProfile: () -> Unit
) {
    Column(Modifier.clickable { goProviderProfile() }.padding(start = 16.dp, end = 16.dp)) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                model = state.businessImage,
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            Column {
                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.agency) + " " + state.businessName,
                    color = secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier,
                    text = "${state.month} ${getTimePeriod(state.month)}",
                    color = grey500,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}
@Serializable
data class ItemProviderState(
    val businessImage: String,
    val businessName: String,
    val month: Int,
)