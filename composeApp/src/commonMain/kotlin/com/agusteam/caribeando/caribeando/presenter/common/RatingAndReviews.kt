package com.agusteam.caribeando.caribeando.presenter.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RatingAndReviews(
    averageRating: Double,
    reviewCount: Long,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Star",
            tint = Color(0xFFFFB400), // Airbnb gold
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "%.2f".format(averageRating),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "($reviewCount reviews)",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
    }
}