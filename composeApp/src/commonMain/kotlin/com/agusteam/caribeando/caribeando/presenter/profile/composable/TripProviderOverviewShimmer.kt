package com.agusteam.caribeando.presenter.profile.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect

@Composable
fun TripProviderOverviewShimmer(){
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect(),
        ) {

        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect(),
                ) {

                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect(),
        ) {

        }
        HorizontalDivider(Modifier)
        Box(
            modifier = Modifier.fillMaxWidth().height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect(),
        ) {

        }
    }

}