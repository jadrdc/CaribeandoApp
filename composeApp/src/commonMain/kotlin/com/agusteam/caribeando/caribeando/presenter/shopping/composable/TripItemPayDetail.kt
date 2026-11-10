package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.shopping.model.ShoppingDetailModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.trip_info

@Composable
fun TripItemPayDetail(details: List<ShoppingDetailModel>) {
    val colors = caribeandoColors
    Column {
        Text(
            text = stringResource(Res.string.trip_info),
            color = colors.textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        details.forEach { item ->
            Column(Modifier.padding(top = 16.dp)) {
                Text(
                    text = item.title,
                    color = colors.textSecondary,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = item.description,
                    color = colors.textPrimary,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        HorizontalDivider(
            color = colors.hairline,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}
