package com.agusteam.caribeando.presenter.common.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
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

/**
 * The handoff's "5. Explorar -- cargando" skeleton: a 216-tall shimmer block radius 18, then a
 * 14-tall bar at 62% width radius 7 (14dp down), then a 12-tall bar at 40% width radius 6 (9dp
 * down). Existing `shimmerEffect`, unchanged.
 *
 * 2026-08-20 (design handoff pass): replaces the old 250-tall/radius-16 card + 3 stacked 48-wide
 * bars + a trailing 60-wide bar layout with the handoff's exact 3-block skeleton.
 */
@Composable
fun TripItemLoading() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
                .clip(RoundedCornerShape(18.dp))
                .shimmerEffect(),
        )
        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .fillMaxWidth(0.62f)
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .shimmerEffect(),
        )
        Box(
            modifier = Modifier
                .padding(top = 9.dp)
                .fillMaxWidth(0.4f)
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .shimmerEffect(),
        )
    }
}
