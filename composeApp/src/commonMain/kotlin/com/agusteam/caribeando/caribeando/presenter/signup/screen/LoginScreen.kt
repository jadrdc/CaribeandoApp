package com.agusteam.caribeando.presenter.signup.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caribeando.composeapp.generated.resources.*
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.EditInputField
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.common.SocialButton
import com.agusteam.caribeando.presenter.signup.viewmodels.LoginEvent
import com.agusteam.caribeando.presenter.signup.viewmodels.LoginEvent.OnUserLogon
import com.agusteam.caribeando.presenter.signup.viewmodels.LoginViewModel
import com.agusteam.caribeando.presenter.theme.primary
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLogin: (TokenMode) -> Unit = {},
    onSignUp: () -> Unit = {},
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo centrado
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_caribeando_logo),
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEventHandler(LoginEvent.OnClickPasswordForgot)
                            },
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        text = stringResource(Res.string.forgot_password),
                        textDecoration = TextDecoration.Underline
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSignUp() },
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        text = stringResource(Res.string.create_account),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            // Zona fija inferior
            Spacer(modifier = Modifier.height(24.dp))

            ActionButton(
                isValid = state.isValid(),
                text = stringResource(Res.string.login),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                viewModel.onEventHandler(LoginEvent.OnLoginProcess)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SocialButton { token ->
                onLogin(token)
            }

            Spacer(modifier = Modifier.height(32.dp))
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
