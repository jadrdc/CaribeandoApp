package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun LinkButton(text: String, onClick: () -> Unit) {
    Text(
        modifier = Modifier.clickable { onClick() },
        text = text,
        color = secondary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        textDecoration = TextDecoration.Underline
    )
}