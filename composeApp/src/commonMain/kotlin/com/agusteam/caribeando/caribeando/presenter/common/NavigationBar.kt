package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun NavigationBar(title: String, onBackPressed: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BackIcon(Modifier) {
            onBackPressed()
        }
        Text(
            color = secondary,
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}