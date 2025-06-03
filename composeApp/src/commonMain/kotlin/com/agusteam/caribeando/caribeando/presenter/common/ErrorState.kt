package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.cancel

@Composable
fun ErrorState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            painter = painterResource(Res.drawable.cancel),
            tint = primary,
            contentDescription = null
        )
        Text(
            text = "Algo salió mal. Por favor, inténtalo de nuevo más tarde.",
            fontSize = 18.sp,
            color = secondary
        )
    }
}