package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TripItemBar(
    isSavedForLater: Boolean,
    onBackPressed: () -> Unit = {},
    onCLick: () -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        BackButton { onBackPressed() }
        // 2026-08-20 (screens 1-17 fidelity pass): spec screen 12's detail-hero heart is
        // "36x36", not FavoriteButton's feed-card default of 34dp.
        FavoriteButton(isSavedForLater = isSavedForLater, size = 36.dp) {
            onCLick()
        }

    }
}