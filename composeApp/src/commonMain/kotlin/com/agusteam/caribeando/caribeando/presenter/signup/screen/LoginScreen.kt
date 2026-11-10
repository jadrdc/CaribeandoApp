package com.agusteam.caribeando.presenter.signup.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caribeando.composeapp.generated.resources.*
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.EditInputField
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.common.SocialButton
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.signup.viewmodels.LoginEvent
import com.agusteam.caribeando.presenter.signup.viewmodels.LoginEvent.OnUserLogon
import com.agusteam.caribeando.presenter.signup.viewmodels.LoginViewModel
import com.agusteam.caribeando.presenter.theme.Radii
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The handoff's screen 1, "Iniciar sesión": "62x62 cream tile radius 18 with the logo at 48x48,
 * 28dp from the top. 26dp down: title. 6dp: subtitle. 28dp: two Fields ... 20dp: PrimaryButton.
 * 16dp: centered TextLink. 24dp: a rule-o-rule divider. Then SecondaryButton [Google]. Pinned to
 * the bottom: '¿Primera vez?' with 'Unirse a la cuenta.'"
 *
 * 2026-08-20 (design handoff pass): the "¿Olvidó su contraseña?" link now navigates to a real
 * screen ([onForgotPassword], wired to `ForgotPasswordScreen`) instead of firing
 * `LoginEvent.OnClickPasswordForgot`, which used to reset the password for whatever email was
 * (or wasn't) typed into THIS screen's email field, with no dedicated screen or confirmation UI.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLogin: (TokenMode) -> Unit = {},
    onSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()

    ObserveAsEvents(event) { event ->
        if (event is OnUserLogon) {
            onLogin(event.user)
        }
    }

    ErrorModal(
        title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null,
        onDismiss = {
            viewModel.onEventHandler(LoginEvent.ClearErrorLogin)
        }
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.pageBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .size(62.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(colors.cream, Radii.cardShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = stringResource(Res.string.login),
                color = colors.textPrimary,
                style = textStyles.screenTitleTabRoot,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(Res.string.login_subtitle),
                color = colors.textSecondary,
                style = textStyles.metadata,
            )
            Spacer(modifier = Modifier.height(28.dp))

            // Inputs scrollables
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    EditInputField(
                        query = state.email,
                        errorText = state.emailError,
                        error = state.isEmailError,
                        labelText = stringResource(Res.string.email),
                        onQueryChange = {
                            viewModel.onEventHandler(LoginEvent.OnEmailChanged(it))
                        }
                    )
                }
                item {
                    EditInputField(
                        keyboardType = KeyboardType.Password,
                        query = state.password,
                        labelText = stringResource(Res.string.password),
                        error = state.isPasswordError,
                        errorText = state.passwordError,
                        onQueryChange = {
                            viewModel.onEventHandler(LoginEvent.OnPasswordChanged(it))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ActionButton(
                isValid = state.isValid(),
                text = stringResource(Res.string.login),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                viewModel.onEventHandler(LoginEvent.OnLoginProcess)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextLink(
                text = stringResource(Res.string.forgot_password),
                onClick = onForgotPassword,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = colors.panelFieldBorder)
                Text(
                    text = stringResource(Res.string.or_divider),
                    color = colors.textSecondary,
                    style = textStyles.metadataSmall,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = colors.panelFieldBorder)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SocialButton(
                onLogin
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(Res.string.first_time),
                    color = colors.textSecondary,
                    style = textStyles.metadata,
                )
                Text(
                    text = " " + stringResource(Res.string.create_account),
                    color = colors.redText,
                    style = textStyles.metadata.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.bounceClick(scaleDown = 0.97f, indication = null) { onSignUp() }
                )
            }
        }

        // Loading overlay
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .bounceClick(enabled = false, indication = null) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.redFill)
            }
        }
    }
}
