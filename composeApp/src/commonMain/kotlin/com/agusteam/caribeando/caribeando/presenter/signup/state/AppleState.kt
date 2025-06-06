package com.agusteam.caribeando.presenter.signup.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel

data class AppleState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val isPasswordError: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: ErrorModel? = null

) : ViewModelState {
    fun isValid(): Boolean {
        return email.isNotBlank() && password.isNotBlank() && !isEmailError && !isPasswordError
    }
}