package com.agusteam.caribeando.presenter.signup.viewmodels

import caribeando.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.getString
import caribeando.composeapp.generated.resources.reset_password_error_message
import caribeando.composeapp.generated.resources.reset_password_error_title
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.RequestResetPasswordEmailUseCase
import com.agusteam.caribeando.domain.validators.FieldValidator
import com.agusteam.caribeando.domain.validators.ValidatorType
import com.agusteam.caribeando.presenter.signup.message
import com.agusteam.caribeando.presenter.signup.state.ForgotPasswordState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * New for the handoff's "¿Olvidó su contraseña?" screen. Reuses the existing
 * `RequestResetPasswordEmailUseCase` (already wired for `LoginScreen`'s inline forgot-password
 * link) with its own screen/state, rather than sharing `LoginViewModel` -- this flow no longer
 * borrows whatever email happens to be typed into the login form.
 */
class ForgotPasswordViewModel(
    private val validator: FieldValidator,
    private val useCase: RequestResetPasswordEmailUseCase,
) : GenericViewModel<ForgotPasswordState, ForgotPasswordEvent>(ForgotPasswordState()) {

    private suspend fun onEmailChanged(value: String) {
        setState { copy(email = value) }
        val result = validator.validateField(ValidatorType.EMAIL, value = value)
        val validationMessage = result.message()
        setState { copy(emailError = validationMessage, isEmailError = result.isError) }
    }

    private suspend fun onErrorHappened(value: Boolean, title: String = "", message: String = "") {
        setState { copy(errorModel = if (!value) null else ErrorModel(title = title, message = message)) }
    }

    private suspend fun submit() {
        if (!state.value.isValid()) return
        setState { copy(isLoading = true) }
        when (val result = useCase(state.value.email)) {
            is OperationResult.Error -> {
                onErrorHappened(
                    true,
                    getString(Res.string.reset_password_error_title),
                    getString(Res.string.reset_password_error_message)
                )
            }

            is OperationResult.Success -> {
                setState { copy(linkSent = true) }
            }
        }
        setState { copy(isLoading = false) }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun onEventHandler(event: ForgotPasswordEvent) {
        viewModelScope.launch(exceptionHandler) {
            when (event) {
                is ForgotPasswordEvent.OnEmailChanged -> onEmailChanged(event.value)
                is ForgotPasswordEvent.OnSubmit -> submit()
                is ForgotPasswordEvent.ClearError -> onErrorHappened(false)
                is ForgotPasswordEvent.ConsumeLinkSent -> setState { copy(linkSent = false) }
            }
        }
    }
}

sealed interface ForgotPasswordEvent {
    class OnEmailChanged(val value: String) : ForgotPasswordEvent
    data object OnSubmit : ForgotPasswordEvent
    data object ClearError : ForgotPasswordEvent
    data object ConsumeLinkSent : ForgotPasswordEvent
}
