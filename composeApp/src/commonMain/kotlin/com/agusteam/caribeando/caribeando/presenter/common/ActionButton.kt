package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.primary

@Composable
fun ActionButton(
    isValid: Boolean = true,
    modifier: Modifier = Modifier,
    text: String, onClick: () -> Unit
) {
    Box(
        modifier = Modifier.background(
            primary.copy(alpha = if (isValid) 1.0f else 0.5f), // Adjust alpha of the color
            RoundedCornerShape(8.dp)
        ).clickable(enabled = isValid) { onClick() }
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = modifier.padding(16.dp),
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}