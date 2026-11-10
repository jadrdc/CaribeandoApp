package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_arrow_back

/**
 * The handoff's "back button" (as opposed to the circular "back circle" floating over hero
 * photos): "38x38 back button (radius 12, #F4F5F6, ic_arrow_back 19)." Used inline in pushed
 * screens' headers via `NavigationBar`.
 */
@Composable
fun BackIcon(modifier: Modifier, onClick: () -> Unit) {
    val colors = caribeandoColors
    Box(
        modifier = modifier
            .size(38.dp)
            .background(colors.fieldFill, Radii.controlShape)
            .bounceClick(scaleDown = 0.92f) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            contentDescription = null,
            modifier = Modifier.size(19.dp),
            tint = colors.textPrimary,
            painter = painterResource(Res.drawable.ic_arrow_back)
        )
    }
}
