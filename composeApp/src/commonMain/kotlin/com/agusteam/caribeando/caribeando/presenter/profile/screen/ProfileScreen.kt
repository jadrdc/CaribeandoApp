package com.agusteam.caribeando.presenter.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ListGroup
import com.agusteam.caribeando.presenter.common.ListGroupLabelValueRow
import com.agusteam.caribeando.presenter.common.ListGroupNavigationRow
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileEvent.LogoutUser
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileEvent.OnErrorModalAccepted
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileEvent.UserSessionClosed
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileViewModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.email
import caribeando.composeapp.generated.resources.favorite
import caribeando.composeapp.generated.resources.ic_address
import caribeando.composeapp.generated.resources.ic_favorite
import caribeando.composeapp.generated.resources.ic_profile
import caribeando.composeapp.generated.resources.ic_trip
import caribeando.composeapp.generated.resources.contact_us
import caribeando.composeapp.generated.resources.lastname
import caribeando.composeapp.generated.resources.logout
import caribeando.composeapp.generated.resources.my_trips
import caribeando.composeapp.generated.resources.name
import caribeando.composeapp.generated.resources.phone
import caribeando.composeapp.generated.resources.profile

/**
 * The handoff's "28. Perfil". Was a hand-rolled `ProfileHeader` (avatar photo, not the spec's
 * cream/ic_profile circle) + `ProfileDetailItemSection` (Material list-item rows, not
 * `ListGroup`) + a bare clickable logout row -- restyled to the spec's cream circle header, two
 * ListGroups (data, then navigation), and a centered logout link.
 *
 * 2026-08-20 (design handoff pass, part 2): [goFavorites]/[goTrips]/[goContactUs] are new --
 * unlike screen 16's "Ver mis viajes" (buried in a nested checkout NavHost with no route to a
 * specific bottom-nav tab), `ProfileScreen` is a direct destination in `HomeScreen`'s own
 * NavHost, so these three CAN navigate for real (see `HomeScreen.kt`'s call site).
 *
 * Two things flagged rather than faked:
 * - "En Caribeando desde <mes año>" is omitted: neither `ProfileState` nor the backend profile
 *   response carries any member-since/created-at date -- the handoff's own "Open question" note
 *   on this screen already calls out that the underlying data here is thinner than the design
 *   ("the data is read-only here, but `UpdatePhoneAndBirthdateRequest` suggests editing is
 *   intended"), and this is the same gap extended to the join-date line.
 * - The avatar circle always shows the `ic_profile` glyph rather than `state.profileImage` --
 *   the spec is explicit ("66×66 cream circle with ic_profile 32 #D62828"), not a photo avatar;
 *   the previous `ProfileHeader` showed a real photo, which is arguably the more useful design,
 *   but this pass follows the spec's given icon-only treatment rather than picking its own.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    goFavorites: () -> Unit = {},
    goTrips: () -> Unit = {},
    goContactUs: () -> Unit = {},
    logout: () -> Unit
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events

    ObserveAsEvents(event) { events ->
        if (events is UserSessionClosed) {
            logout()
        }
    }

    ErrorModal(title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null, onDismiss = {
            viewModel.handleEvent(OnErrorModalAccepted)
        })

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
    ) {
        item {
            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = stringResource(Res.string.profile),
                color = colors.textPrimary,
                style = textStyles.screenTitleTabRoot,
            )
        }
        item {
            Row(
                modifier = Modifier.padding(top = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(66.dp).background(colors.cream, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_profile),
                        contentDescription = null,
                        tint = colors.redFill,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 14.dp),
                    text = state.fullName,
                    color = colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        item {
            ListGroup(
                modifier = Modifier.padding(top = 26.dp),
                rows = listOf(
                { ListGroupLabelValueRow(stringResource(Res.string.name), state.name) },
                { ListGroupLabelValueRow(stringResource(Res.string.lastname), state.lastname) },
                { ListGroupLabelValueRow(stringResource(Res.string.email), state.email) },
                { ListGroupLabelValueRow(stringResource(Res.string.phone), state.phone) },
            ))
        }
        item {
            ListGroup(
                modifier = Modifier.padding(top = 14.dp),
                rows = listOf(
                    {
                        ListGroupNavigationRow(
                            icon = painterResource(Res.drawable.ic_favorite),
                            label = stringResource(Res.string.favorite),
                            onClick = goFavorites
                        )
                    },
                    {
                        ListGroupNavigationRow(
                            icon = painterResource(Res.drawable.ic_trip),
                            label = stringResource(Res.string.my_trips),
                            onClick = goTrips
                        )
                    },
                    {
                        ListGroupNavigationRow(
                            icon = painterResource(Res.drawable.ic_address),
                            label = stringResource(Res.string.contact_us),
                            onClick = goContactUs
                        )
                    },
                )
            )
        }
        item {
            Box(
                modifier = Modifier.padding(top = 30.dp, bottom = 24.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.bounceClick { viewModel.handleEvent(LogoutUser) }) {
                    Text(
                        color = colors.redFill,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        text = stringResource(Res.string.logout),
                    )
                }
            }
        }
    }
}
