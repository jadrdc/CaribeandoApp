package com.agusteam.caribeando.domain.validators

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldValidatorTest {

    private val validator = FieldValidator()

    @Test
    fun blankValuesAreNeverReportedAsErrors() {
        ValidatorType.entries.forEach { type ->
            assertFalse(validator.validateField(type, "").isError, "blank $type should not be an error")
        }
    }

    @Test
    fun emailAcceptsValidAndRejectsInvalid() {
        assertFalse(validator.validateField(ValidatorType.EMAIL, "user@example.com").isError)
        assertEquals(
            ValidationError.EMAIL_INVALID,
            validator.validateField(ValidatorType.EMAIL, "user@example").error
        )
        assertTrue(validator.validateField(ValidatorType.EMAIL, "not-an-email").isError)
    }

    @Test
    fun phoneRequiresDigitsOnly() {
        assertFalse(validator.validateField(ValidatorType.PHONE, "8492605023").isError)
        assertTrue(validator.validateField(ValidatorType.PHONE, "849-260-5023").isError)
        assertTrue(validator.validateField(ValidatorType.PHONE, "12345").isError)
    }

    @Test
    fun passwordNeedsUppercaseDigitSpecialAndEightChars() {
        assertFalse(validator.validateField(ValidatorType.PASSWORD, "Test1234@").isError)
        assertTrue(validator.validateField(ValidatorType.PASSWORD, "test1234@").isError, "no uppercase")
        assertTrue(validator.validateField(ValidatorType.PASSWORD, "Testtest@").isError, "no digit")
        assertTrue(validator.validateField(ValidatorType.PASSWORD, "Test12345").isError, "no special")
        assertTrue(validator.validateField(ValidatorType.PASSWORD, "Te1@").isError, "too short")
    }

    @Test
    fun confirmationMustMatchPassword() {
        assertFalse(validator.validateField(ValidatorType.PASSWORD_SAME, "Test1234@", "Test1234@").isError)
        val mismatch = validator.validateField(ValidatorType.PASSWORD_SAME, "Test1234@", "Other1234@")
        assertTrue(mismatch.isError)
        assertEquals(ValidatorType.PASSWORD_SAME, mismatch.validatorType)
        assertEquals(ValidationError.PASSWORDS_MISMATCH, mismatch.error)
    }
}
