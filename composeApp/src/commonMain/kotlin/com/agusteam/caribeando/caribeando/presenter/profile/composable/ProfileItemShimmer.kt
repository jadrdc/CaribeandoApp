package com.agusteam.caribeando.presenter.profile.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect

@Composable
fun ProfileItemShimmer() {
    Row(Modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(CircleShape)
                .size(32.dp)
                .shimmerEffect()
        )
        Column {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .height(16.dp)
                    .width(48.dp)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .height(16.dp)
                    .width(48.dp)
                    .shimmerEffect()
            )
        }
    }

}