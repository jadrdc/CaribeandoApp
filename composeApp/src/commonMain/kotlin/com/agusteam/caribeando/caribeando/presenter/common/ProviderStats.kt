package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun ProviderStatItem(value: String, label: String, showDivider: Boolean = true) {
    val maxWidth = remember { mutableStateOf(0) }
    Column {
        Text(
            text = value,
            color = secondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp, modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    maxWidth.value = maxOf(maxWidth.value, coordinates.size.width)
                }
        )
        Text(
            text = label,
            color = secondary,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp, modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    maxWidth.value = maxOf(maxWidth.value, coordinates.size.width)
                }
        )
        if (showDivider)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
                    .width(with(LocalDensity.current) { maxWidth.value.toDp() })
            )

    }
}