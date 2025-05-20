package com.agusteam.caribeando.presenter.profile.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect

@Composable
fun ProfileHeader(isLoading: Boolean, value: String) {
    if (isLoading) {
        Box(
            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth().height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect(),
        ) {

        }
    } else {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = value,
            fontSize = 35.sp, lineHeight = 40.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}