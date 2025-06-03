package com.agusteam.caribeando.presenter.wishlist.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect

@Composable
fun WishItemLoading(){
    Box(
        Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)).shimmerEffect()
    ) {

    }
}