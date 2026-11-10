package com.agusteam.caribeando.presenter.signup

import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.validation_email_invalid
import caribeando.composeapp.generated.resources.validation_passwords_must_match
import caribeando.composeapp.generated.resources.validation_password_invalid
import caribeando.composeapp.generated.resources.validation_phone_invalid
import com.agusteam.caribeando.domain.validators.ValidationError
import com.agusteam.caribeando.domain.validators.ValidationResult
import org.jetbrains.compose.resources.getString

/** Localized error text for a field, or an empty string when the field is valid. */
suspend fun ValidationResult.message(): String = when (error) {
    null -> ""
    ValidationError.PASSWORDS_MISMATCH -> getString(Res.string.validation_passwords_must_match)
    ValidationError.EMAIL_INVALID -> getString(Res.string.validation_email_invalid)
    ValidationError.PHONE_INVALID -> getString(Res.string.validation_phone_invalid)
    ValidationError.PASSWORD_INVALID -> getString(Res.string.validation_password_invalid)
}
