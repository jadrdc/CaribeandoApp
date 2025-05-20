package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.grey500
import com.agusteam.caribeando.presenter.theme.primary
import com.agusteam.caribeando.presenter.theme.secondary

@Composable
fun CustomRadioButton(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) { // Use weight to allocate space dynamically
            Text(
                text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = secondary
            )
            Text(
                text = description,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = grey500,
                modifier = Modifier.padding(top = 4.dp).widthIn(150.dp)
            )
        }
        Box(
            modifier = Modifier.padding(start = 8.dp) // Add padding here to create space
        ) {
            RadioButton(
                colors = RadioButtonDefaults.colors(selectedColor = primary),
                selected = isSelected,
                onClick = { onClick() },
            )
        }
    }
}