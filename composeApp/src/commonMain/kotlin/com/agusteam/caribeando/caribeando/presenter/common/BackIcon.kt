package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ic_arrow_back

@Composable
fun BackIcon(modifier: Modifier, onClick: () -> Unit) {
    Icon(
        contentDescription = null,
        modifier = modifier.size(24.dp).clickable { onClick() },
        painter = painterResource(Res.drawable.ic_arrow_back)
    )
}