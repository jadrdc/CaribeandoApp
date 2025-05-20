package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.ok

@Composable
fun ErrorModal(
    showError: Boolean, // State to show/hide the dialog
    title: String, message: String, onDismiss: () -> Unit // Callback to handle dismiss action
) {
    if (showError) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = title, fontSize = 18.sp
                    )
                    Text(
                        text = message, style = MaterialTheme.typography.bodyMedium
                    )
                    ActionButton(
                        text = stringResource(Res.string.ok),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        onDismiss()
                    }
                }
            }
        }
    }
}
