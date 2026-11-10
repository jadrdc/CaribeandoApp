package com.agusteam.caribeando.presenter.signup.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.confirm_password
import caribeando.composeapp.generated.resources.email
import caribeando.composeapp.generated.resources.finish_signup
import caribeando.composeapp.generated.resources.lastname
import caribeando.composeapp.generated.resources.name
import caribeando.composeapp.generated.resources.password
import caribeando.composeapp.generated.resources.phone
import caribeando.composeapp.generated.resources.signup
import caribeando.composeapp.generated.resources.terms
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.EditInputField
import com.agusteam.caribeando.presenter.common.ErrorModal
import com.agusteam.caribeando.presenter.common.ModernDatePicker
import com.agusteam.caribeando.presenter.common.NavigationBar
import com.agusteam.caribeando.presenter.common.ObserveAsEvents
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.localDateToInstant
import com.agusteam.caribeando.presenter.signup.viewmodels.SignUpViewModel
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


/**
 * The handoff's screen 2, "Completa tu cuenta": back button, title, "Nombre + Apellido side by
 * side (11dp gap)," then Correo/Teléfono/Contraseña/Confirmar contraseña, a terms paragraph, and
 * PrimaryButton "Completar cuenta."
 *
 * 2026-08-20 (design handoff pass): the handoff's field list doesn't include a birthdate --
 * kept `ModernDatePicker` anyway, since `SignUpViewModel`'s `isValid()` requires a birthdate and
 * the backend's `signUpUser` call takes one; dropping it would break account creation. This is a
 * real gap in the handoff, not a judgment call about matching it exactly.
 */
@Composable
fun SignUpAccountScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
    onSignUpSuccess: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val event = viewModel.events
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()

    ObserveAsEvents(event) { event ->
        if (event is SignUpViewModel.SignUpEvent.FillRemainingInfo) {
            onSignUpSuccess()
        }
    }
    ErrorModal(title = state.errorModel?.title ?: "",
        message = state.errorModel?.message ?: "",
        showError = state.errorModel != null, onDismiss = {
            viewModel.onEventHandler(SignUpViewModel.SignUpEvent.ClearError)
        })
    Box {
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    NavigationBar(title = stringResource(Res.string.finish_signup)) { onBackPressed() }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                    ) {
                        EditInputField(
                            modifier = Modifier.weight(1f),
                            query = state.name,
                            labelText = stringResource(Res.string.name),
                            onQueryChange = {
                                viewModel.onEventHandler(
                                    SignUpViewModel.SignUpEvent.OnNameChanged(it)
                                )
                            },
                        )
                        EditInputField(
                            modifier = Modifier.weight(1f),
                            query = state.lastName,
                            labelText = stringResource(Res.string.lastname),
                            onQueryChange = {
                                viewModel.onEventHandler(
                                    SignUpViewModel.SignUpEvent.OnLastNameChanged(it)
                                )
                            },
                        )
                    }
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
                                SignUpViewModel.SignUpEvent.OnPhoneNumberChanged(
                                    it
                                )
                            )
                        }, modifier = Modifier
                    )
                }
                item {
                    EditInputField(
                        query = state.email,
                        errorText = state.emailError,
                        error = state.isEmailError,
                        labelText = stringResource(Res.string.email),
                        onQueryChange = {
                            viewModel.onEventHandler(SignUpViewModel.SignUpEvent.OnEmailChanged(it))
                        }, modifier = Modifier
                    )
                }
                item {
                    EditInputField(
                        keyboardType = KeyboardType.Password,
                        query = state.password,
                        labelText = stringResource(Res.string.password),
                        error = state.isPasswordError,
                        onQueryChange = {
                            viewModel.onEventHandler(
                                SignUpViewModel.SignUpEvent.OnPasswordChanged(
                                    it
                                )
                            )
                        },
                        modifier = Modifier,
                        errorText = state.passwordError,
                    )
                }
                item {
                    EditInputField(
                        keyboardType = KeyboardType.Password,
                        query = state.confirmPassword,
                        error = state.isSamePasswordError,
                        labelText = stringResource(Res.string.confirm_password),
                        onQueryChange = {
                            viewModel.onEventHandler(
                                SignUpViewModel.SignUpEvent.OnConfirmPasswordChanged(
                                    it
                                )
                            )
                        },
                        modifier = Modifier,
                        errorText = state.samePasswordError,
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
                    // 2026-08-20 (design handoff pass): "the terms paragraph at 400/10.5/1.6 #5F6A70"
                    // -- was a generic 14sp/Normal default-colored line.
                    Text(
                        text = stringResource(Res.string.terms),
                        style = textStyles.bodyDescription.copy(
                            fontSize = 10.5.sp,
                            lineHeight = 16.8.sp,
                        ),
                        color = colors.textSecondary,
                    )
                }
            }
            ActionButton(
                isValid = state.isValid(),
                text = stringResource(Res.string.signup),
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.onEventHandler(SignUpViewModel.SignUpEvent.SignUp)
            }
        }
        // Loading overlay
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .bounceClick(enabled = false, indication = null) {}, // Prevents interaction
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.redFill)
            }
        }
    }
}
