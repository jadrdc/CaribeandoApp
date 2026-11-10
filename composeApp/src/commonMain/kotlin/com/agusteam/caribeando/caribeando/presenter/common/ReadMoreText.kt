package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.read_less
import caribeando.composeapp.generated.resources.read_more

// 2026-08-20 (dark-mode audit): both colors here were raw, theme-blind `Colors.kt` vals.
// `grey500` (body text) never lightened for dark mode, dropping below AA contrast against the
// dark page background. `secondary` (navy, the "Leer más/menos" toggle) was ALSO wrong
// independent of dark mode -- the handoff's spec (grep "Leer más") wants this link "600/12.5
// #D62828", not navy -- and would have gone illegibly dark-navy-on-dark-background on top of
// being the wrong hue. Fixed both: `colors.grey500` for the body, `colors.redText` for the
// toggle (the text/icon red role, since `redFill` fails contrast as text on a dark background).
@Composable
fun ReadMoreText(
    text: String,
    collapsedMaxLines: Int = 5
) {
    val colors = caribeandoColors
    var isExpanded by remember { mutableStateOf(false) }
    val toggleText =
        if (isExpanded) stringResource(Res.string.read_less) else stringResource(Res.string.read_more)

    Column {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = colors.grey500,
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = toggleText,
            color = colors.redText,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(top = 8.dp)
        )
    }
}