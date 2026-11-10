package com.agusteam.caribeando.presenter.signup.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.RequestResetPasswordEmailUseCase
import com.agusteam.caribeando.domain.validators.FieldValidator
import com.agusteam.caribeando.domain.validators.ValidatorType
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
        setState { copy(emailError = result.errorMessage, isEmailError = result.isError) }
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
                    "Error al reiniciar la contraseña",
                    "No se pudo enviar el email de reinicio de contraseña."
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
