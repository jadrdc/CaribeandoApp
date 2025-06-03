package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DateTripRowInformation(dataFrom: String, dateTo: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Align items vertically
    ) {
        DateComponent("Fecha de salida", dataFrom)
        Box(
            Modifier
                .height(40.dp) // Adjust height as needed
                .width(1.dp)
                .background(Color.Gray) // Customize divider color
        )
        Box(
            Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(Color.Red) // Color del separador
        )
        DateComponent("Fecha de regreso", dateTo)
    }
}