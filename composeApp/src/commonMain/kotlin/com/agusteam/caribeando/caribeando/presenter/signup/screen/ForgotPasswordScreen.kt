package com.agusteam.caribeando.presenter.signup.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.back_to_login
import caribeando.composeapp.generated.resources.email
import caribeando.composeapp.generated.resources.forgot_password_subtitle
import caribeando.composeapp.generated.resources.forgot_password_title
import caribeando.composeapp.generated.resources.reset_link_sent_toast
import caribeando.composeapp.generated.resources.send_link
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.EditInputField
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.common.Toast
import com.agusteam.caribeando.presenter.signup.viewmodels.ForgotPasswordEvent
import com.agusteam.caribeando.presenter.signup.viewmodels.ForgotPasswordViewModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The handoff's screen 3, "¿Olvidó su contraseña?" -- [NEW] screen, but the backend already
 * exists (`RequestResetPasswordEmailUseCase`). Previously this flow had no screen of its own: the
 * login screen's inline "forgot password" link reused whatever was in the login form's own email
 * field and showed the result via `ErrorModal` (a generic dialog, not a toast).
 *
 * The spec says "Submitting returns to login with the toast 'Te enviamos un enlace a tu correo.'"
 * There's no app-wide toast/snackbar host to hand a message across screens to, so this shows the
 * `Toast` on THIS screen for its normal 2200ms auto-dismiss window, then calls [onLinkSent] --
 * same user-facing outcome (confirmation, then back to login) without new cross-screen state.
 */
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onLinkSent: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()

    ErrorModal(
        title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null,
        onDismiss = { viewModel.onEventHandler(ForgotPasswordEvent.ClearError) }
    )

    Box(Modifier.fillMaxSize().background(colors.pageBackground)) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(8.dp))
            NavigationBar(
                title = stringResource(Res.string.forgot_password_title),
                onBackPressed = onBackPressed
            )
            Spacer(Modifier.height(8.dp))
            androidx.compose.material3.Text(
                text = stringResource(Res.string.forgot_password_subtitle),
                color = colors.textSecondary,
                style = textStyles.bodyDescription,
            )
            Spacer(Modifier.height(24.dp))
            EditInputField(
                query = state.email,
                labelText = stringResource(Res.string.email),
                error = state.isEmailError,
                errorText = state.emailError,
                keyboardType = KeyboardType.Email,
                onQueryChange = { viewModel.onEventHandler(ForgotPasswordEvent.OnEmailChanged(it)) }
            )
            Spacer(Modifier.height(20.dp))
            ActionButton(
                isValid = state.isValid(),
                text = stringResource(Res.string.send_link),
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.onEventHandler(ForgotPasswordEvent.OnSubmit)
            }
            Spacer(Modifier.weight(1f))
            // 2026-08-20 (screens 1-17 fidelity pass): spec wants this link at 500/12.5 in
            // #003049 (navy) -- muted's size/weight match, but muted's color is grey and
            // underlined, neither of which this screen's spec asks for.
            TextLink(
                text = stringResource(Res.string.back_to_login),
                onClick = onBackPressed,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                muted = true,
                color = colors.textPrimary,
                underline = false,
            )
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colors.redFill)
            }
        }

        val toastMessage = if (state.linkSent) stringResource(Res.string.reset_link_sent_toast) else null
        Toast(
            message = toastMessage,
            onDismissed = {
                viewModel.onEventHandler(ForgotPasswordEvent.ConsumeLinkSent)
                onLinkSent()
            }
        )
    }
}
