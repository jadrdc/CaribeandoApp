package com.agusteam.caribeando.domain.validators

class FieldValidator {

    fun validateField(
        validatorType: ValidatorType, value: String, value2: String = ""
    ): ValidationResult {
        return when (validatorType) {
            ValidatorType.EMAIL -> {
                validateEmail(value)
            }

            ValidatorType.PHONE -> {
                validatePhone(value)
            }

            ValidatorType.PASSWORD -> {
                validatePassword(value)
            }

            ValidatorType.PASSWORD_SAME -> {
                validateSamePassword(validatorType, value, value2)
            }
        }

    }

    private fun validateSamePassword(
        validatorType: ValidatorType, value: String, value2: String = ""
    ): ValidationResult {
        return if (value.isBlank() || value2.isBlank()) {
            ValidationResult(validatorType = validatorType)
        } else if (value == value2) {
            ValidationResult(validatorType = ValidatorType.PASSWORD_SAME)
        } else {
            ValidationResult(
                validatorType, ValidationError.PASSWORDS_MISMATCH
            )
        }
    }

    private fun validateEmail(email: String): ValidationResult {
        return if (email.isBlank()) {
            ValidationResult(validatorType = ValidatorType.EMAIL)
        } else if (email.matches(Regex(EMAIL_REGEX))) {
            ValidationResult(validatorType = ValidatorType.EMAIL)
        } else {
            ValidationResult(
                ValidatorType.EMAIL, ValidationError.EMAIL_INVALID
            )
        }
    }

    private fun validatePhone(phone: String): ValidationResult {

        return if (phone.isBlank()) {
            ValidationResult(validatorType = ValidatorType.PHONE)
        } else if (phone.matches(Regex(PHONE_REGEX))) {
            ValidationResult(validatorType = ValidatorType.PHONE)
        } else {
            ValidationResult(
                ValidatorType.PHONE, ValidationError.PHONE_INVALID
            )
        }
    }

    private fun validatePassword(password: String): ValidationResult {
        return if (password.isBlank()) {
            ValidationResult(validatorType = ValidatorType.PASSWORD)
        } else if (password.matches(Regex(PASSWORD_REGEX))) {
            ValidationResult(validatorType = ValidatorType.PASSWORD)
        } else {
            ValidationResult(
                ValidatorType.PASSWORD, ValidationError.PASSWORD_INVALID
            )
        }
    }
}


/** Why a field is invalid. The presentation layer turns this into localized text. */
enum class ValidationError { PASSWORDS_MISMATCH, EMAIL_INVALID, PHONE_INVALID, PASSWORD_INVALID }

data class ValidationResult(
    val validatorType: ValidatorType,
    val error: ValidationError? = null
) {
    val isError: Boolean get() = error != null
}
