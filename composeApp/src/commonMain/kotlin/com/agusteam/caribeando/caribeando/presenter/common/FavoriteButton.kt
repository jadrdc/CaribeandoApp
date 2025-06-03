package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_favorite
import caribeando.composeapp.generated.resources.ic_favorite_filled

@Composable
fun FavoriteButton(
    isSavedForLater: Boolean,
    onFavoriteToggle: () -> Unit
) {
    Box(
        Modifier.clip(CircleShape).size(32.dp).background(Color.White, CircleShape)
            .clickable(interactionSource = null, indication = null) {
                onFavoriteToggle()
            }) {
        Icon(
            tint = if (isSavedForLater) Color.Red else Color.Black,
            modifier = Modifier.size(24.dp).align(Alignment.Center),
            contentDescription = null,
            painter = painterResource(
                if (isSavedForLater) Res.drawable.ic_favorite_filled
                else Res.drawable.ic_favorite
            ),
        )
    }
}