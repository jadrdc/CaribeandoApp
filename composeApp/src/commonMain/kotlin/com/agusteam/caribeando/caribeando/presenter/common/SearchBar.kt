package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect
import com.agusteam.caribeando.presenter.theme.greyColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_filter
import caribeando.composeapp.generated.resources.ic_search
import caribeando.composeapp.generated.resources.search_hint

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    isloadingn: Boolean,
    onSearchBarClick: (Boolean) -> Unit
) {
    if (isloadingn) {
        Box(
            modifier = Modifier.fillMaxWidth().height(45.dp).clip(RoundedCornerShape(16.dp))
                .shimmerEffect(),
        ) {

        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth().padding(bottom = 16.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(25.dp))
                .background(color = Color.White, shape = RoundedCornerShape(25.dp))
                .padding(8.dp).clickable(interactionSource = null, indication = null) {
                    onSearchBarClick(true)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null, modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(Res.string.search_hint),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Box(
                Modifier.size(40.dp).clip(CircleShape).border(1.dp, greyColor, CircleShape)
            ) {
                Icon(
                    modifier = Modifier.size(32.dp).align(Alignment.Center),
                    contentDescription = null,
                    painter = painterResource(Res.drawable.ic_filter),
                )
            }
        }
    }
}