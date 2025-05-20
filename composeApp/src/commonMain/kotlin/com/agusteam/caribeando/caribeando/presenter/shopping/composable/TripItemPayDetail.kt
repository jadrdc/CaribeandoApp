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
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.trip_info

@Composable
fun TripItemPayDetail(details:List<ShoppingDetailModel>) {
    Column {
        Text(
            text = stringResource(Res.string.trip_info),
            color = secondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        details.forEach { item ->
            Column(Modifier.padding(top = 16.dp)) {
                Text(
                    text = item.title,
                    color = secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = item.description,
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