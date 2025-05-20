package com.agusteam.caribeando.presenter.signup.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.caribeandoapp
import caribeando.composeapp.generated.resources.create_account
import caribeando.composeapp.generated.resources.email
import caribeando.composeapp.generated.resources.forgot_password
import caribeando.composeapp.generated.resources.loading
import caribeando.composeapp.generated.resources.login
import caribeando.composeapp.generated.resources.password
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

    ErrorModal(title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null,
        onDismiss = {
            viewModel.onEventHandler(LoginEvent.ClearErrorLogin)
        })


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            item {
                Box(
                    Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp).size(180.dp)
                        .clip(CircleShape), contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(180.dp),
                        painter = painterResource(Res.drawable.caribeandoapp),
                        contentDescription = null
                    )
                }
            }
            item {
                Box(Modifier.padding(top = 16.dp)) {
                    EditInputField(
                        query = state.email,
                        errorText = state.emailError,
                        error = state.isEmailError,
                        labelText = stringResource(Res.string.email),
                        onQueryChange = {
                            viewModel.onEventHandler(LoginEvent.OnEmailChanged(it))
                        },
                        modifier = Modifier
                    )
                }
            }
            item {
                Box(Modifier.padding(top = 16.dp)) {
                    EditInputField(
                        keyboardType = KeyboardType.Password,
                        query = state.password,
                        labelText = stringResource(Res.string.password),
                        error = state.isPasswordError,
                        onQueryChange = {
                            viewModel.onEventHandler(LoginEvent.OnPasswordChanged(it))
                        },
                        modifier = Modifier,
                        errorText = state.passwordError,
                    )
                }
            }
            item {
                Box(Modifier.padding(top = 24.dp).clickable {
                    viewModel.onEventHandler(LoginEvent.OnClickPasswordForgot)
                }) {
                    Text(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        text = stringResource(Res.string.forgot_password),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            item {
                Box(Modifier.padding(top = 24.dp)) {
                    Text(
                        modifier = Modifier.clickable { onSignUp() },
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        text = stringResource(Res.string.create_account),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            item {
                Box(Modifier.padding(top = 40.dp)) {
                    ActionButton(
                        isValid = state.isValid(),
                        text = stringResource(Res.string.login),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.onEventHandler(LoginEvent.OnLoginProcess)
                    }
                }
            }
            item {
                SocialButton()
            }
        }

        // Loading overlay
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .clickable(enabled = false) {}, // Prevents interaction
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primary)
            }
        }
    }
}
