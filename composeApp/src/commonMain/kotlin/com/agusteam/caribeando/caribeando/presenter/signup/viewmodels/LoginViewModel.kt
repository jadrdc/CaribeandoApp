package com.agusteam.caribeando.presenter.signup.viewmodels

import caribeando.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.getString
import caribeando.composeapp.generated.resources.login_error_title
import caribeando.composeapp.generated.resources.reset_password_email_sent
import caribeando.composeapp.generated.resources.reset_password_error_message
import caribeando.composeapp.generated.resources.reset_password_error_title
import caribeando.composeapp.generated.resources.reset_password_invalid_email
import caribeando.composeapp.generated.resources.reset_password_title
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.domain.usecase.LoginUseCase
import com.agusteam.caribeando.domain.usecase.RequestResetPasswordEmailUseCase
import com.agusteam.caribeando.domain.usecase.SaveTokenDataUseCase
import com.agusteam.caribeando.domain.validators.FieldValidator
import com.agusteam.caribeando.domain.validators.ValidatorType
import com.agusteam.caribeando.presenter.signup.message
import com.agusteam.caribeando.presenter.signup.state.LoginState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginViewModel(
    private val logger:CrashReporter,
    private val validator: FieldValidator,
    private val useCase: LoginUseCase,
    private val saveTokenDataUseCase: SaveTokenDataUseCase,
    private val requestResetPasswordEmailUseCase: RequestResetPasswordEmailUseCase
) : GenericViewModel<LoginState, LoginEvent>(LoginState()) {


    private suspend fun setValidationError(
        validatorType: ValidatorType,
        value: String,
        value2: String = ""
    ) {
        val result = validator.validateField(validatorType, value = value, value2 = value2)
        val validationMessage = result.message()
        when (result.validatorType) {
            ValidatorType.EMAIL -> {
                setState {
                    copy(
                        emailError = validationMessage, isEmailError = result.isError
                    )
                }
            }

            ValidatorType.PHONE, ValidatorType.PASSWORD, ValidatorType.PASSWORD_SAME -> {

            }
        }

    }

    private suspend fun onEmailChanged(value: String) {
        setState {
            copy(email = value)
        }
        setValidationError(ValidatorType.EMAIL, value)
    }

    private suspend fun onPasswordChanged(value: String) {
        setState {
            copy(password = value)
        }
    }

    private suspend fun onErrorHappened(value: Boolean, title: String = "", message: String = "") {
        val errorModel = if (!value) {
            null
        } else {
            ErrorModel(title = title, message = message)
        }
        setState {
            copy(
                errorModel = errorModel
            )
        }
    }

    private suspend fun login() {
        setState { copy(isLoading = true) }
        when (val result = useCase(state.value.email, state.value.password)) {
            is OperationResult.Error -> {
                logger.recordException(result.exception)
                onErrorHappened(
                    true,
                    getString(Res.string.login_error_title),
                    result.exception.message ?: ""
                )
            }

            is OperationResult.Success -> {
                val userModel = result.data
                saveTokenDataUseCase(userModel)
                sendEvent(LoginEvent.OnUserLogon(userModel))
            }
        }
        setState { copy(isLoading = false) }
    }

    private suspend fun requestEmail() {
        setState { copy(isLoading = true) }
        when (val result=requestResetPasswordEmailUseCase(state.value.email)) {
            is OperationResult.Error -> {
                logger.recordException(result.exception)
                onErrorHappened(
                    true,
                    getString(Res.string.reset_password_error_title),
                    getString(Res.string.reset_password_error_message)
                )
            }

            is OperationResult.Success -> {
                onErrorHappened(
                    true,
                    getString(Res.string.reset_password_title),
                    getString(Res.string.reset_password_email_sent)
                )
            }
        }
        setState { copy(isLoading = false) }

    }

    private suspend fun onPasswordReset() {
        val result = validator.validateField(ValidatorType.EMAIL, value = state.value.email)
        if (result.isError || state.value.email.isBlank()) {
            onErrorHappened(
                true,
                getString(Res.string.reset_password_error_title),
                getString(Res.string.reset_password_invalid_email)
            )
        } else {
            requestEmail()
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.message
    }

    fun onEventHandler(event: LoginEvent) {
        viewModelScope.launch(exceptionHandler) {
            when (event) {
                is LoginEvent.OnPasswordChanged -> {
                    onPasswordChanged(event.value)
                }

                is LoginEvent.OnEmailChanged -> {
                    onEmailChanged(event.value)
                }

                is LoginEvent.OnLoginProcess -> {
                    login()
                }

                is LoginEvent.ClearErrorLogin -> {
                    onErrorHappened(false)
                }

                is LoginEvent.OnClickPasswordForgot -> {
                    onPasswordReset()
                }

                else -> {

                }
            }
        }
    }
}

sealed interface LoginEvent {
    data object OnLoginProcess : LoginEvent
    data class OnUserLogon(val user: TokenMode) : LoginEvent
    class OnEmailChanged(val value: String) : LoginEvent
    class OnPasswordChanged(val value: String) : LoginEvent
    data object ClearErrorLogin : LoginEvent
    data object OnClickPasswordForgot : LoginEvent
}