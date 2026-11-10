package com.agusteam.caribeando.caribeando.presenter.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.logo

/**
 * The handoff's "EmptyState": "Centered column, 60dp from the top of the content area. 78x78
 * cream tile radius 22 holding the logo at 58x58, radius 16, 55% opacity. 20dp down: title
 * 600/16 #003049. 6dp down: one line of 400/12.5 #5F6A70. 18dp down: a TextLink action. This
 * replaces empty_state.png (1.4 MB) in the current EmptyState."
 *
 * 2026-08-20 (design handoff pass): [message] now plays the "title" role (the spec's more
 * prominent 600/16 line) since every existing call site passes a single short headline-like
 * string (e.g. "No hay viajes que mostrar"), not a supporting caption. Added an optional
 * [subtitle] for the spec's second, lighter line -- existing call sites don't pass one yet
 * (that's a per-screen copy fix, not a token/component fix), so it's absent until each screen's
 * pass fills in the exact handoff copy. Signature otherwise unchanged so all 4 existing call
 * sites keep compiling.
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    val colors = caribeandoColors
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(
            modifier = Modifier
                .size(78.dp)
                .background(colors.cream, Radii.tileShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = null,
                alpha = 0.55f,
                modifier = Modifier.size(58.dp).clip(RoundedCornerShape(16.dp))
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = message,
            color = colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = colors.textSecondary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(18.dp))
            TextLink(text = actionText, onClick = onAction)
        }
    }
}
