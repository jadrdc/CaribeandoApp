package com.agusteam.caribeando.presenter.profile.screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import com.agusteam.caribeando.presenter.profile.viewmodels.ContactUsViewModel
import caribeando.composeapp.generated.resources.contact_whatsapp_detail_suffix
import caribeando.composeapp.generated.resources.contact_email_detail_suffix
import caribeando.composeapp.generated.resources.contact_us_subtitle_hours
import caribeando.composeapp.generated.resources.contact_us_subtitle_generic
import caribeando.composeapp.generated.resources.contact_unavailable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import com.agusteam.caribeando.presenter.common.ListGroup
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.chevron_right_small
import caribeando.composeapp.generated.resources.contact_email_detail
import caribeando.composeapp.generated.resources.contact_go_my_trips
import caribeando.composeapp.generated.resources.contact_phone_detail
import caribeando.composeapp.generated.resources.contact_phone_label
import caribeando.composeapp.generated.resources.contact_reported_trip_description
import caribeando.composeapp.generated.resources.contact_reported_trip_title
import caribeando.composeapp.generated.resources.contact_us
import caribeando.composeapp.generated.resources.contact_us_subtitle
import caribeando.composeapp.generated.resources.contact_whatsapp_detail
import caribeando.composeapp.generated.resources.contact_whatsapp_label
import caribeando.composeapp.generated.resources.email

/**
 * The handoff's "30. Contáctanos" -- entirely new, linked from Perfil.
 *
 * 2026-10-09: the channel rows now come from the backend (GET /support/contact, see
 * ContactUsViewModel) -- the placeholder values described below are gone.
 *
 * 2026-08-20 (design handoff pass, part 2): the three channel rows use the handoff's OWN
 * placeholder copy verbatim (the spec text itself says "Replace these with real contact
 * details" -- these aren't invented here, they're the spec's given example values, kept until
 * the business provides real ones). Each row opens a real `tel:`/`mailto:`/`wa.me` link via
 * Compose's `LocalUriHandler` -- the same dependency-free mechanism `WhatsAppButton.kt` already
 * established elsewhere in this codebase -- rather than being a dead tap; if the phone/email get
 * swapped for real values, only the string resources need to change, not this wiring.
 * "Ir a mis viajes" navigates for real (`goMyTrips`), since `ContactUsScreen` sits as a direct
 * destination in `HomeScreen`'s own NavHost (see that file's call site).
 */
@Composable
fun ContactUsScreen(
    onBackPressed: () -> Unit,
    goMyTrips: () -> Unit,
    viewModel: ContactUsViewModel = koinViewModel(),
) {
    val colors = caribeandoColors
    val uriHandler = LocalUriHandler.current
    // 2026-10-09: channels now come from the backend (GET /support/contact); a blank value
    // hides that row. Replaces the handoff's placeholder 809 555 0100 / hola@caribeando.do.
    val state by viewModel.state.collectAsStateWithLifecycle()
    val contact = state.contact
    val whatsappLabel = stringResource(Res.string.contact_whatsapp_label)
    val emailLabel = stringResource(Res.string.email)
    val phoneLabel = stringResource(Res.string.contact_phone_label)
    val whatsappDetail = stringResource(Res.string.contact_whatsapp_detail_suffix)
    val emailDetail = stringResource(Res.string.contact_email_detail_suffix)
    val channelRows: List<@Composable () -> Unit> = buildList<@Composable () -> Unit> {
        val wa = contact?.whatsapp.orEmpty()
        if (wa.isNotBlank()) add {
            ContactChannelRow(Icons.Filled.Chat, whatsappLabel, whatsappDetail) {
                uriHandler.openUri("https://wa.me/$wa")
            }
        }
        val mail = contact?.email.orEmpty()
        if (mail.isNotBlank()) add {
            ContactChannelRow(Icons.Filled.Email, emailLabel, "$mail · $emailDetail") {
                uriHandler.openUri("mailto:$mail")
            }
        }
        val phone = contact?.phone.orEmpty()
        if (phone.isNotBlank()) add {
            ContactChannelRow(Icons.Filled.Phone, phoneLabel, phone) {
                uriHandler.openUri("tel:" + phone.filter { it.isDigit() || it == '+' })
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
    ) {
        item {
            Box(Modifier.padding(top = 22.dp)) {
                NavigationBar(title = stringResource(Res.string.contact_us)) { onBackPressed() }
            }
        }
        item {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = contact?.hours?.takeIf { it.isNotBlank() }
                    ?.let { stringResource(Res.string.contact_us_subtitle_hours, it) }
                    ?: stringResource(Res.string.contact_us_subtitle_generic),
                color = colors.textSecondary,
                fontSize = 12.5.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
        item {
            when {
                state.isLoading -> Box(
                    Modifier.fillMaxWidth().padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = colors.redFill) }
                channelRows.isNotEmpty() -> ListGroup(
                    modifier = Modifier.padding(top = 24.dp),
                    rows = channelRows,
                )
                else -> Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = stringResource(Res.string.contact_unavailable),
                    color = colors.textSecondary,
                    fontSize = 12.5.sp,
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(top = 22.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .background(colors.fieldFill, Radii.panelShape)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.contact_reported_trip_title),
                    color = colors.textPrimary,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(Res.string.contact_reported_trip_description),
                    color = colors.textSecondary,
                    fontSize = 11.5.sp,
                    lineHeight = 18.4.sp,
                    fontWeight = FontWeight.Normal
                )
                Box(Modifier.padding(top = 12.dp).bounceClick { goMyTrips() }) {
                    Text(
                        text = stringResource(Res.string.contact_go_my_trips),
                        color = colors.redFill,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactChannelRow(
    icon: ImageVector,
    label: String,
    detail: String,
    onClick: () -> Unit,
) {
    val colors = caribeandoColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.98f) { onClick() }
            .padding(horizontal = 15.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = colors.textPrimary, modifier = Modifier.size(19.dp))
        Column(Modifier.weight(1f)) {
            Text(text = label, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = detail,
                color = colors.textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Icon(
            painter = painterResource(Res.drawable.chevron_right_small),
            contentDescription = null,
            tint = colors.iconInactive,
            modifier = Modifier.size(16.dp)
        )
    }
}
