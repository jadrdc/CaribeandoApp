package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_filter
import caribeando.composeapp.generated.resources.ic_search
import caribeando.composeapp.generated.resources.search_hint

/**
 * The handoff's "SearchBar": "Height 46, fill #F4F5F6, radius 14, 14dp horizontal padding, 9dp
 * gap. ic_search at 17 in #5F6A70, then placeholder 400/13 #5F6A70. Trailing 46x46 square, radius
 * 14, fill #003049, ic_filter at 19 in white. The whole bar opens the filter sheet -- matching
 * today's SearchBar behavior. The search input itself lives inside the sheet."
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    isloadingn: Boolean,
    onSearchBarClick: (Boolean) -> Unit
) {
    val colors = caribeandoColors
    if (isloadingn) {
        Box(
            modifier = Modifier.fillMaxWidth().height(46.dp).clip(Radii.buttonShape)
                .shimmerEffect(),
        ) {

        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth().height(46.dp)
                .background(color = colors.fieldFill, shape = Radii.buttonShape)
                .bounceClick(scaleDown = 0.98f) { onSearchBarClick(true) }
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(17.dp)
                )
                Text(
                    text = stringResource(Res.string.search_hint),
                    color = colors.textSecondary,
                    style = caribeandoTextStyles().bodyDescription.copy(fontSize = 13.sp),
                    modifier = Modifier.padding(start = 9.dp)
                )
            }

            Box(
                Modifier.size(46.dp).clip(Radii.buttonShape).background(colors.navyFill, Radii.buttonShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(19.dp),
                    tint = Color.White,
                    contentDescription = null,
                    painter = painterResource(Res.drawable.ic_filter),
                )
            }
        }
    }
}
