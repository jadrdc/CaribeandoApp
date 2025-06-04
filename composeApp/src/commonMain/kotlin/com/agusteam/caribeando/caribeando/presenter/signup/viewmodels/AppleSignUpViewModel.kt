package com.agusteam.caribeando.presenter.signup.viewmodels

import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.models.TokenMode
import com.agusteam.caribeando.domain.usecase.AppleSignUpUseCase
import com.agusteam.caribeando.domain.usecase.SaveTokenDataUseCase
import com.agusteam.caribeando.presenter.signup.state.AppleState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class AppleSignUpViewModel(
    private val logger:CrashReporter,
    private val useCase: AppleSignUpUseCase,
    private val saveTokenDataUseCase: SaveTokenDataUseCase,
) : GenericViewModel<AppleState, AppleEvent>(AppleState()) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.message
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

    private suspend fun signUp(token: String, firstName: String, lastName: String) {
        setState { copy(isLoading = true) }
        when (val result = useCase(token, firstName, lastName)) {
            is OperationResult.Error -> {
                logger.recordException(result.exception)
                onErrorHappened(
                    true,
                    "Error de Inicio de Sesión",
                    result.exception.message ?: ""
                )
            }

            is OperationResult.Success -> {
                val userModel = result.data
                saveTokenDataUseCase(userModel)
                sendEvent(AppleEvent.Success(userModel))
            }
        }
        setState { copy(isLoading = false) }
    }


    fun onEventHandler(event: AppleEvent) {
        viewModelScope.launch(exceptionHandler) {
            when (event) {
                is AppleEvent.SignUp -> {
                    signUp(event.token, event.firstName, event.lastName)
                }

                else -> {

                }
            }
        }
    }
}

sealed interface AppleEvent {
    data class SignUp(val token: String, val firstName: String, val lastName: String) : AppleEvent
    data class Success(val data: TokenMode) : AppleEvent
}