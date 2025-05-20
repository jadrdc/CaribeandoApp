package com.agusteam.caribeando.presenter.signup.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel

data class LoginState(
    val email: String = "joseagustinreinoso@gmail.com",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val password: String = "joseagustinreinoso@gmail.com",
    val passwordError: String = "",
    val isPasswordError: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: ErrorModel? = null

) : ViewModelState {
    fun isValid(): Boolean {
        return email.isNotBlank() && password.isNotBlank() && !isEmailError && !isPasswordError
    }
}
