package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.caribeando.presenter.common.RatingAndReviews
import com.agusteam.caribeando.presenter.common.ReadMoreText
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun ShoppingItemOverview(
    title: String, description: String,
    rating: Double = 0.0, reviewCount: Int = 0,
    modifier: Modifier
) {

    Column(modifier) {
        Text(text = title, color = secondary, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Box(Modifier.padding(vertical = 8.dp)) {
            RatingAndReviews(averageRating = rating, reviewCount = reviewCount.toLong())
        }
        if (description.isNotEmpty()) {
            ReadMoreText(
                text = description,
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp),
        )
    }

}