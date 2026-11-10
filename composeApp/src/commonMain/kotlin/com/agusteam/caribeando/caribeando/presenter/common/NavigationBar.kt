package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles

/**
 * The handoff's "Screen title (pushed screens)": 25/1.25/600/-0.02em, next to the 38x38 back
 * button. Used across SignUpAccountScreen, FillSignUpAccountScreen, TripProviderProfileScreen,
 * ReportOrderIssueScreen, TripItemPayHeader -- restyling here is a one-shot fix for all of them,
 * same as the earlier shared-component pass.
 */
@Composable
fun NavigationBar(title: String, onBackPressed: () -> Unit) {
    val colors = caribeandoColors
    Row(verticalAlignment = Alignment.CenterVertically) {
        BackIcon(Modifier) {
            onBackPressed()
        }
        Text(
            color = colors.textPrimary,
            text = title,
            style = caribeandoTextStyles().screenTitlePushed,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
