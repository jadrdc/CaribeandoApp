package com.agusteam.caribeando.presenter.common

import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.contact_us
import caribeando.composeapp.generated.resources.error_page_subtitle
import caribeando.composeapp.generated.resources.error_page_title
import caribeando.composeapp.generated.resources.try_again
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.cancel

/**
 * The handoff's "11. Error de página": full-screen centered "cancel" icon at 100 in #D62828,
 * 24dp down the headline at 500/18/1.45 (verbatim copy), 12dp down a supporting line, then a
 * PrimaryButton "Intentar de nuevo" and a TextLink "Contáctanos".
 *
 * 2026-08-20 (design handoff pass): added a real [onRetry] callback -- the previous version had
 * no button at all, a dead end with no way to recover short of leaving the screen. Both existing
 * call sites (ExploreScreen, WishListScreen) now wire it to their own refresh event. [onContactUs]
 * is optional and left unwired for now (no "Contáctanos" flow exists yet anywhere in the app --
 * that's the Cuenta flow's still-pending "Contáctanos" screen); the TextLink only renders once a
 * caller supplies it, rather than shipping a button that does nothing.
 */
@Composable
fun ErrorState(
    onRetry: () -> Unit,
    onContactUs: (() -> Unit)? = null,
) {
    val colors = caribeandoColors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(horizontal = 26.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            painter = painterResource(Res.drawable.cancel),
            tint = colors.redFill,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(Res.string.error_page_title),
            fontSize = 18.sp,
            lineHeight = 26.1.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = colors.textPrimary
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(Res.string.error_page_subtitle),
            fontSize = 12.5.sp,
            textAlign = TextAlign.Center,
            color = colors.textSecondary
        )
        Column(Modifier.padding(top = 28.dp).fillMaxWidth()) {
            ActionButton(
                text = stringResource(Res.string.try_again),
                modifier = Modifier.fillMaxWidth(),
                onClick = onRetry
            )
            if (onContactUs != null) {
                Column(Modifier.padding(top = 16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    TextLink(text = stringResource(Res.string.contact_us), onClick = onContactUs)
                }
            }
        }
    }
}
