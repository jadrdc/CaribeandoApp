package com.agusteam.caribeando.presenter.signup

import com.agusteam.caribeando.presenter.signup.state.SignupState
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

class SignupStateTest {

    private val valid = SignupState(
        name = "Jose",
        lastName = "Reinoso",
        email = "user@example.com",
        password = "Test1234@",
        confirmPassword = "Test1234@",
        phone = "8492605023",
        birthdate = Instant.fromEpochSeconds(0)
    )

    @Test
    fun completeFormIsValid() = assertTrue(valid.isValid())

    @Test
    fun missingNameOrLastNameDisablesSubmit() {
        assertFalse(valid.copy(name = "").isValid())
        assertFalse(valid.copy(lastName = " ").isValid())
    }

    @Test
    fun missingBirthdateDisablesSubmit() = assertFalse(valid.copy(birthdate = null).isValid())

    @Test
    fun passwordConfirmationMustMatch() =
        assertFalse(valid.copy(confirmPassword = "Different1@").isValid())

    @Test
    fun phoneMustHaveTenDigits() {
        assertFalse(valid.copy(phone = "849260502").isValid())
        assertFalse(valid.copy(phone = "84926050234").isValid())
    }

    @Test
    fun fieldErrorsDisableSubmit() {
        assertFalse(valid.copy(isEmailError = true).isValid())
        assertFalse(valid.copy(isPasswordError = true).isValid())
        assertFalse(valid.copy(isPhoneError = true).isValid())
    }

    @Test
    fun fillingInfoNeedsPhoneAndBirthdate() {
        assertTrue(SignupState(phone = "8492605023", birthdate = Instant.fromEpochSeconds(0)).isFillingInfoCompleted())
        assertFalse(SignupState(phone = "8492605023").isFillingInfoCompleted())
        assertFalse(SignupState(birthdate = Instant.fromEpochSeconds(0)).isFillingInfoCompleted())
    }
}
