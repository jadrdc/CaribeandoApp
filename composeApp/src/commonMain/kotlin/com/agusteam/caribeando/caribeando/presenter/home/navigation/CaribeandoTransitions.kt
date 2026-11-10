package com.agusteam.caribeando.presenter.home.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

/**
 * 2026-10-09 (design handoff, "Interactions & behavior -> Transitions"):
 * "Push / pop a screen: 300ms, cubic-bezier(.2,.8,.2,1), fade + 14px horizontal slide."
 * The 14px offset is expressed as a fraction of the screen width (~1/28 of a 390pt phone).
 * Tab roots (bottom nav) crossfade instead of sliding, since they are siblings, not a stack.
 */
object CaribeandoTransitions {
    val easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)
    private const val DURATION = 300
    private const val TAB_DURATION = 200

    val enter: EnterTransition =
        fadeIn(tween(DURATION, easing = easing)) + slideInHorizontally(tween(DURATION, easing = easing)) { it / 28 }
    val exit: ExitTransition =
        fadeOut(tween(DURATION, easing = easing)) + slideOutHorizontally(tween(DURATION, easing = easing)) { -it / 28 }
    val popEnter: EnterTransition =
        fadeIn(tween(DURATION, easing = easing)) + slideInHorizontally(tween(DURATION, easing = easing)) { -it / 28 }
    val popExit: ExitTransition =
        fadeOut(tween(DURATION, easing = easing)) + slideOutHorizontally(tween(DURATION, easing = easing)) { it / 28 }

    val tabEnter: EnterTransition = fadeIn(tween(TAB_DURATION))
    val tabExit: ExitTransition = fadeOut(tween(TAB_DURATION))
}
