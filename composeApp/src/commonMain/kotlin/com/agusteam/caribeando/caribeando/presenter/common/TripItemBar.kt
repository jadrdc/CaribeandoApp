package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TripItemBar(
    isSavedForLater: Boolean,
    onBackPressed: () -> Unit = {},
    onCLick: () -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        BackButton { onBackPressed() }
        FavoriteButton(isSavedForLater = isSavedForLater) {
            onCLick()
        }

    }
}