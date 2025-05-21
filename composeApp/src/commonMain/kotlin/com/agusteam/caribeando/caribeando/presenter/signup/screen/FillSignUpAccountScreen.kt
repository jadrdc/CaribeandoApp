package com.agusteam.caribeando.presenter.signup.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.confirm_password
import caribeando.composeapp.generated.resources.email
import caribeando.composeapp.generated.resources.finish_signup
import caribeando.composeapp.generated.resources.ic_caribeando_logo
import caribeando.composeapp.generated.resources.lastname
import caribeando.composeapp.generated.resources.name
import caribeando.composeapp.generated.resources.password
import caribeando.composeapp.generated.resources.phone
import caribeando.composeapp.generated.resources.signup
import caribeando.composeapp.generated.resources.terms
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.EditInputField
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ModernDatePicker
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.localDateToInstant
import com.agusteam.caribeando.presenter.signup.viewmodels.SignUpViewModel
import com.agusteam.caribeando.presenter.theme.primary
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun FillSignUpAccountScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    onLogin:()->Unit,
    onBackPressed: () -> Unit = {}
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events
    ObserveAsEvents(event) { event ->
        if (event is SignUpViewModel.SignUpEvent.GoHome) {
            onLogin()
        }
    }

    ErrorModal(
        title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null,
        onDismiss = { viewModel.onEventHandler(SignUpViewModel.SignUpEvent.ClearError) }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    NavigationBar(title = stringResource(Res.string.finish_signup)) { onBackPressed() }
                }
                item {
                    // Center the logo horizontally
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_caribeando_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(180.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    EditInputField(
                        usePhoneMask = true,
                        errorText = state.phoneError,
                        error = state.isPhoneError,
                        keyboardType = KeyboardType.Phone,
                        query = state.phone,
                        labelText = stringResource(Res.string.phone),
                        onQueryChange = {
                            viewModel.onEventHandler(
                                SignUpViewModel.SignUpEvent.OnPhoneNumberChanged(it)
                            )
                        }
                    )
                }
                item {
                    ModernDatePicker(onDateSelected = {
                        viewModel.onEventHandler(
                            SignUpViewModel.SignUpEvent.OnBirthdateChanged(
                                localDateToInstant(it, TimeZone.currentSystemDefault())
                            )
                        )
                    })
                }
                item {
                    Text(
                        text = stringResource(Res.string.terms),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            ActionButton(
                isValid = state.isFillingInfoCompleted(),
                text = stringResource(Res.string.signup),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                viewModel.onEventHandler(SignUpViewModel.SignUpEvent.FillRemainingInfo)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Loading overlay
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primary)
            }
        }
    }
}