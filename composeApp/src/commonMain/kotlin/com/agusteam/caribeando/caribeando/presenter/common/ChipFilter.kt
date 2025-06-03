package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChipFilter(title: String, icon: DrawableResource, isSelected: Boolean) {
    FilterChip(
        onClick = { },
        label = { Text(text = title) },
        selected = isSelected,
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Localized description",
                Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )
}