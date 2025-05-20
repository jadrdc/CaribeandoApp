package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.agusteam.caribeando.domain.models.TripProviderModel
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun ProviderOverViewItem(
    modifier: Modifier = Modifier,
    tripProviderModel: TripProviderModel
) {
    Column(
        modifier.shadow(
            elevation = 8.dp, // Adjust elevation as needed
            shape = RoundedCornerShape(8.dp),
            ambientColor = Color.Black.copy(alpha = 0.1f), // Soft shadow color
            spotColor = Color.Black.copy(alpha = 0.2f)
        ).fillMaxWidth().background(Color.White, shape = RoundedCornerShape(8.dp))

    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                AsyncImage(
                    modifier = Modifier.size(104.dp).clip(CircleShape),
                    model = tripProviderModel.avatarUrl,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = tripProviderModel.name,
                    color = secondary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp
                )
            }
            Column(Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp)) {
                ProviderStatsItem(tripProviderModel = tripProviderModel)
            }
        }
    }
}