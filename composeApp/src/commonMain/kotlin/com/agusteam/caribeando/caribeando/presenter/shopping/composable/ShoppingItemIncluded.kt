package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun ShoppingItemIncluded(
    title: String,
    items: List<String>,
    modifier: Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        items.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Box(Modifier.padding(top = 4.dp)) {
                    Icon(
                        tint = secondary,
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp),
                    text = it,
                    color = grey500,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}