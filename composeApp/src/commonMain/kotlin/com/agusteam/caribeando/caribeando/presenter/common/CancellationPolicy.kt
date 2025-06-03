package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.chevron_right_small

@Composable
fun CancellationPolicy(
    modifier: Modifier,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(modifier.clickable {
        onClick()
    }) {
        Text(
            text = title,
            color = secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = description,
                color = grey500,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f) // Let the text take available space
                ,
                fontWeight = FontWeight.Normal
            )
            Icon(
                painter = painterResource(Res.drawable.chevron_right_small),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}