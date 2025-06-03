package com.agusteam.caribeando.presenter.profile.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agusteam.caribeando.domain.models.ProfileDataModel
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.common.effects.shimmerEffect
import com.agusteam.caribeando.presenter.profile.composable.ProfileDetailItemSection
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileEvent.LogoutUser
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileEvent.OnErrorModalAccepted
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileEvent.UserSessionClosed
import com.agusteam.caribeando.presenter.profile.viewmodels.ProfileViewModel
import com.agusteam.caribeando.presenter.theme.primary
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.email
import caribeando.composeapp.generated.resources.lastname
import caribeando.composeapp.generated.resources.logout
import caribeando.composeapp.generated.resources.name
import caribeando.composeapp.generated.resources.phone
import com.agusteam.caribeando.presenter.profile.composable.ProfileHeader

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    logout: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events

    ObserveAsEvents(event) { events ->
        if (events is UserSessionClosed) {
            logout()
        }
    }

    val profiles =
        listOf(
            ProfileDataModel(
                title = stringResource(Res.string.name),
                description = state.name,
                icon = Icons.Filled.Person
            ),
            ProfileDataModel(
                title = stringResource(Res.string.lastname),
                description = state.lastname,
                icon = Icons.Filled.AccountBox
            ),
            ProfileDataModel(
                title = stringResource(Res.string.email),
                description = state.email,
                icon = Icons.Filled.Email
            ),
            ProfileDataModel(
                title = stringResource(Res.string.phone),
                description = state.phone,
                icon = Icons.Filled.Phone
            )
        )
    ErrorModal(title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null, onDismiss = {
            viewModel.handleEvent(OnErrorModalAccepted)
        })

    Column(
        Modifier.padding(horizontal = 16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader(
            isLoading = state.isLoading, value = state.fullName,
            imageUrl = state.profileImage
        )
        ProfileDetailItemSection(isLoading = state.isLoading, profileList = profiles)
        if (state.isLoading) {
            Box(
                modifier = Modifier.padding(top = 40.dp).align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(8.dp))
                    .height(16.dp)
                    .width(60.dp)
                    .shimmerEffect()
            )
        } else {
            Box(Modifier.padding(top = 24.dp).align(Alignment.CenterHorizontally).clickable {
                viewModel.handleEvent(LogoutUser)
            }) {
                Text(
                    color = primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End,
                    text = stringResource(Res.string.logout),
                )
            }
        }
    }
}