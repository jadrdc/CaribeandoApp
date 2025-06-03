package com.agusteam.caribeando.presenter.common.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect

@Composable
fun TripItemLoading() {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp))
                .shimmerEffect(),
        ) {

        }
        Row(
            Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier) {
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
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .height(16.dp)
                        .width(48.dp)
                        .shimmerEffect()
                )
            }


            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .height(16.dp)
                    .width(60.dp)
                    .shimmerEffect()
            )
        }
    }

}