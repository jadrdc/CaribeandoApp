package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.stripe.PaymentState
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.pay_trip

@Composable
fun TripItemPayHeader(
    state: PaymentState,
    onBackPressed: () -> Unit
) {

    Column {
        NavigationBar(title = stringResource(Res.string.pay_trip)) { onBackPressed() }
        Row(
            modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)),
                model = state.profilePhoto,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(120.dp)
            ) {
                Column {
                    Text(
                        modifier = Modifier,
                        text = state.title,
                        color = secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier,
                        text = state.destiny,
                        color = grey500,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                Column {
                    Text(
                        color = secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = "Fecha"
                    )
                    Text(text = state.leavingTime, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}