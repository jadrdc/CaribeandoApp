package com.agusteam.caribeando.presenter.signup.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.FillUserInfoUseCase
import com.agusteam.caribeando.domain.usecase.SaveTokenDataUseCase
import com.agusteam.caribeando.domain.usecase.SignUpUseCase
import com.agusteam.caribeando.domain.validators.FieldValidator
import com.agusteam.caribeando.domain.validators.ValidatorType
import com.agusteam.caribeando.presenter.signup.state.SignupState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime


class SignUpViewModel(
    private val logger: CrashReporter,
    private val validator: FieldValidator,
    private val fillUserInfoUseCase: FillUserInfoUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val saveTokenDataUseCase: SaveTokenDataUseCase,
) : GenericViewModel<SignupState, SignUpViewModel.SignUpEvent>(SignupState()) {

    private suspend fun signUp() {
        setState { copy(isLoading = true) }
        val birthDate: String =
            state.value.birthdate?.toLocalDateTime(TimeZone.currentSystemDefault())?.date.toString()

        if (birthDate.isNotBlank())
            when (val result = signUpUseCase(
                name = state.value.name,
                lastName = state.value.lastName,
                phone = state.value.phone,
                email = state.value.email,
                password = state.value.password,
                birthdate = birthDate
            )) {
                is OperationResult.Error -> {
                    logger.recordException(result.exception)
                    onErrorHappened(
                        true,
                        "Error al registrar la cuenta",
                        message = result.exception.message ?: " "
                    )
                }

                is OperationResult.Success -> {
                    saveTokenDataUseCase(result.data)
                    sendEvent(SignUpEvent.FillRemainingInfo)
                }
            }
        setState { copy(isLoading = false) }

    }

    private suspend fun onNameChanged(value: String) {
        setState {
            copy(name = value)
        }
    }

    private suspend fun onLastNameChanged(value: String) {
        setState {
            copy(lastName = value)
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

    private suspend fun onPhoneChanged(value: String) {
        setState {
            copy(phone = value)
        }
        setValidationError(ValidatorType.PHONE, value)
    }

    private suspend fun setValidationError(
        validatorType: ValidatorType, value: String, value2: String = ""
    ) {
        val result = validator.validateField(validatorType, value = value, value2 = value2)
        when (result.validatorType) {
            ValidatorType.EMAIL -> {
                setState {
                    copy(
                        emailError = result.errorMessage, isEmailError = result.isError
                    )
                }
            }

            ValidatorType.PHONE -> {
                setState {
                    copy(
                        phoneError = result.errorMessage, isPhoneError = result.isError
                    )

                }
            }

            ValidatorType.PASSWORD -> {
                if (!result.isError) {
                    setValidationError(ValidatorType.PASSWORD_SAME, value, value2)
                }
                setState {
                    copy(
                        passwordError = result.errorMessage, isPasswordError = result.isError
                    )
                }
            }

            ValidatorType.PASSWORD_SAME -> {
                setState {
                    copy(
                        samePasswordError = result.errorMessage,
                        isSamePasswordError = result.isError
                    )
                }
            }
        }

    }

    private suspend fun onEmailChanged(value: String) {
        setState {
            copy(email = value)
        }
        setValidationError(ValidatorType.EMAIL, value)
    }

    private suspend fun onBirthdateChanged(value: Instant) {
        setState {
            copy(birthdate = value)
        }
    }

    private suspend fun onPasswordChanged(value: String) {
        setState {
            copy(password = value)
        }
        setValidationError(ValidatorType.PASSWORD, value)
    }

    private suspend fun onPasswordConfirmChanged(value: String) {
        setState {
            copy(confirmPassword = value)
        }
        setValidationError(ValidatorType.PASSWORD_SAME, state.value.password, value)
    }

    fun onEventHandler(event: SignUpEvent) {
        viewModelScope.launch {
            when (event) {
                is SignUpEvent.OnConfirmPasswordChanged -> {
                    onPasswordConfirmChanged(event.value)
                }

                is SignUpEvent.FillRemainingInfo -> {
                    setState { copy(isLoading = true) }
                    val birthDate: String =
                        state.value.birthdate?.toLocalDateTime(TimeZone.currentSystemDefault())?.date.toString()
                    if (birthDate.isNotBlank()) {
                        val result =
                            fillUserInfoUseCase(
                                state.value.phone,
                                birthDate
                            )
                        when (result) {
                            is OperationResult.Error -> {
                                logger.recordException(result.exception)
                            }

                            is OperationResult.Success -> {
                                saveTokenDataUseCase(result.data)
                                sendEvent(SignUpEvent.GoHome)
                            }
                        }
                    }
                    setState { copy(isLoading = false) }

                }

                is SignUpEvent.OnEmailChanged -> {
                    onEmailChanged(event.value)
                }

                is SignUpEvent.OnLastNameChanged -> {
                    onLastNameChanged(event.value)
                }

                is SignUpEvent.OnNameChanged -> {
                    onNameChanged(event.value)
                }

                is SignUpEvent.OnPasswordChanged -> {
                    onPasswordChanged(event.value)
                }

                is SignUpEvent.OnPhoneNumberChanged -> {
                    onPhoneChanged(event.value)
                }

                is SignUpEvent.SignUp -> {
                    signUp()
                }

                is SignUpEvent.ClearError -> {
                    onErrorHappened(false)
                }

                is SignUpEvent.OnBirthdateChanged -> {
                    onBirthdateChanged(event.data)
                }

                SignUpEvent.GoHome -> {

                }
            }
        }
    }

    sealed interface SignUpEvent {
        data object SignUp : SignUpEvent
        data object FillRemainingInfo : SignUpEvent
        data object ClearError : SignUpEvent
        data object GoHome : SignUpEvent
        data class OnBirthdateChanged(val data: Instant) : SignUpEvent
        class OnNameChanged(val value: String) : SignUpEvent
        class OnLastNameChanged(val value: String) : SignUpEvent
        class OnPhoneNumberChanged(val value: String) : SignUpEvent
        class OnEmailChanged(val value: String) : SignUpEvent
        class OnPasswordChanged(val value: String) : SignUpEvent
        class OnConfirmPasswordChanged(val value: String) : SignUpEvent
    }
}
