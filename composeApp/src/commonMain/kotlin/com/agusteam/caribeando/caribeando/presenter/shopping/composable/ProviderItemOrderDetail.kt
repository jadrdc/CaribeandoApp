package com.agusteam.caribeando.presenter.shopping.composable

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
import androidx.compose.ui.Alignment
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
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.agency

@Composable
fun ProviderItemOrderDetail(
    businessName: String,
    businessPhoto: String, businessMonth: String,
    showDivider: Boolean = true
) {
    Column(Modifier.clickable { }) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                model = businessPhoto,
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            Column {
                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.agency) + " " + businessName,
                    color = secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier,
                    text = "$businessMonth ${getTimePeriod(businessMonth.toInt())}",
                    color = grey500,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        if (showDivider)
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
            )
    }
}