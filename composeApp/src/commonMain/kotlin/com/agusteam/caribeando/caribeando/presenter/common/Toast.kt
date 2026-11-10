package com.agusteam.caribeando.presenter.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import kotlinx.coroutines.delay

/**
 * The handoff's "Toast": "Pinned 26dp from the bottom, 20dp side insets. #003049 fill, radius 14,
 * padding 13 x 16, label 500/12.5 white, centered. Auto-dismisses after 2200ms."
 *
 * Call from inside a `Box` that fills the screen (or any `BoxScope`), e.g. the root `Box` of a
 * screen -- this only draws the pill itself, aligned to the bottom, and is responsible for its own
 * appear/dismiss animation + auto-dismiss timer. The caller owns the `message` state (null = no
 * toast shown) and should null it out via [onDismissed] so re-showing the same message re-triggers
 * the timer.
 */
@Composable
fun BoxScope.Toast(
    message: String?,
    onDismissed: () -> Unit,
) {
    val colors = caribeandoColors
    AnimatedVisibility(
        visible = message != null,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontal = 20.dp, vertical = 26.dp),
        enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 },
        exit = fadeOut(tween(300)),
    ) {
        LaunchedEffect(message) {
            if (message != null) {
                delay(2200)
                onDismissed()
            }
        }
        Box(
            modifier = Modifier
                .background(colors.navyFill, Radii.buttonShape)
                .padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            Text(
                text = message.orEmpty(),
                color = Color.White,
                textAlign = TextAlign.Center,
                style = caribeandoTextStyles().chipLabel,
            )
        }
    }
}
