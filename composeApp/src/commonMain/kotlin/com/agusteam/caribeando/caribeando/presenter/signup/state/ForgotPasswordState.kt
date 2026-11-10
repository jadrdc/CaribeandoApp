package com.agusteam.caribeando.presenter.signup.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel

/** New for the handoff's "¿Olvidó su contraseña?" screen -- previously this flow had no screen
 * of its own; the login screen triggered the reset email inline using whatever was typed into
 * its own email field. */
data class ForgotPasswordState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val isLoading: Boolean = false,
    val linkSent: Boolean = false,
    val errorModel: ErrorModel? = null,
) : ViewModelState {
    fun isValid(): Boolean = email.isNotBlank() && !isEmailError
}
