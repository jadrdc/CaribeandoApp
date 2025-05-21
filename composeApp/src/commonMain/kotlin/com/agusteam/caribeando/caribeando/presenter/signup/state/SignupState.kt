package com.agusteam.caribeando.presenter.signup.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.ZoneOffset
import kotlin.time.Duration.Companion.days

data class SignupState(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phone: String = "",
    val passwordError: String = "",
    val isPasswordError: Boolean = false,
    val samePasswordError: String = "",
    val isSamePasswordError: Boolean = false,
    val phoneError: String = "",
    val isPhoneError: Boolean = false,
    val isLoading: Boolean = false,
    val birthdate: Instant = Clock.System.now().minus((18 * 365).days),
    val errorModel: ErrorModel? = null
) : ViewModelState {
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword == password &&
                phone.isNotBlank() && phone.length == 10
    }
}