package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.cancel
import caribeando.composeapp.generated.resources.ok

/**
 * The handoff's "10. Error (modal)": scrim, then a centered card, full width minus 26dp insets,
 * white, radius 18, 20dp padding. "cancel" icon 22 #D62828 + title 600/16 in a row, 12dp down the
 * message at 400/12.5/1.6 #5F6A70, 20dp down a PrimaryButton "Ok".
 *
 * 2026-08-20 (design handoff pass): signature kept identical -- this has 10 existing call sites
 * across shopping/signup/explore/profile/orders. The shared `ActionButton` (radius 14) is used
 * for the "Ok" button rather than a one-off radius-12 box, for consistency with every other
 * PrimaryButton in the app; the handoff's per-screen "radius 12" note here reads like small
 * numeric drift against its own shared "PrimaryButton: radius 14" spec entry.
 */
@Composable
fun ErrorModal(
    showError: Boolean,
    title: String, message: String, onDismiss: () -> Unit
) {
    if (showError) {
        val colors = caribeandoColors
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                // 2026-08-20 (dark-mode audit): was hardcoded `Color.White` while the
                // title/message below already read theme-reactive `colors.*` roles (flipping to
                // light colors in dark mode) -- rendered near-white text on a white card,
                // effectively invisible in dark mode.
                color = colors.pageBackground,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 13.dp),
                shape = Radii.cardShape,
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.cancel),
                            contentDescription = null,
                            tint = colors.redFill,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = message,
                        fontSize = 12.5.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = colors.textSecondary
                    )
                    Column(Modifier.padding(top = 20.dp)) {
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
}
