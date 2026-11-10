package com.agusteam.caribeando.presenter.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.flow.collectLatest

/**
 * 2026-08-19 (interactivity pass): a shared "press-scale" tap effect for every tappable card,
 * button, and thumbnail in the app. The element springs down to [scaleDown] while pressed and
 * bounces back to 1f on release/cancel, giving a consistent tactile feel across screens instead
 * of the previous instant, no-feedback taps. This is the standard Compose community pattern for
 * combining a scale animation with a clickable's real press state (via a shared
 * MutableInteractionSource + PressInteraction), and mirrors the "motion as feedback" guidance in
 * Material's touch-target motion spec.
 *
 * Pass `indication = null` at call sites (like FavoriteButton) that intentionally suppress the
 * ripple -- the press-scale still applies on top of that.
 */
@Composable
fun Modifier.bounceClick(
    enabled: Boolean = true,
    scaleDown: Float = 0.96f,
    indication: Indication? = LocalIndication.current,
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = true
                is PressInteraction.Release, is PressInteraction.Cancel -> isPressed = false
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "bounceClickScale"
    )

    return this
        .scale(scale)
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick
        )
}
