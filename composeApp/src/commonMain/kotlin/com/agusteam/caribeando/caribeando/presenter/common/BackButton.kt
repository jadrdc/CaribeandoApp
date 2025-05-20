package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(onClick: () -> Unit) {
    Box(Modifier.clip(CircleShape).size(32.dp).background(Color.White, CircleShape)) {
        BackIcon(Modifier.align(Alignment.Center)){onClick()}
    }
}