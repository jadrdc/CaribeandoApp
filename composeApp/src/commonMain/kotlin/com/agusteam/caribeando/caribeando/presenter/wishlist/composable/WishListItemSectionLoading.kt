package com.agusteam.caribeando.presenter.wishlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun WishListItemSectionLoading() {
    Column(
        Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .height(32.dp)
                .width(100.dp)
                .shimmerEffect()
        )

        Column(Modifier) {
            Row(
                Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                WishItemLoading()
                WishItemLoading()
            }
            Row(
                Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                WishItemLoading()
                WishItemLoading()
            }
            Row(
                Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                WishItemLoading()
                WishItemLoading()
            }
        }
    }
}