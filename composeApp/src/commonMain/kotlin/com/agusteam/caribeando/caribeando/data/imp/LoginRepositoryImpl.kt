package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.model.AppleSignUpRequest
import com.agusteam.caribeando.data.model.GoogleTokenRequest
import com.agusteam.caribeando.data.model.LoginRequest
import com.agusteam.caribeando.data.model.RequestPasswordChangeModel
import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.data.model.UpdatePhoneAndBirthdateRequest
import com.agusteam.caribeando.data.model.UserSignUpRequest
import com.agusteam.caribeando.data.network.services.SignUpService
import com.agusteam.caribeando.domain.interfaces.LoginRepository
import com.agusteam.caribeando.domain.models.TokenMode
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class LoginRepositoryImpl(private val service: SignUpService) : LoginRepository {

    override suspend fun apple(
        identityToken: String,
        firstName: String?,
        lastName: String?
    ): OperationResult<TokenMode> {

        return try {
            when (val result = service.appleSignUp(
                AppleSignUpRequest(
                    identityToken = identityToken,
                    lastName = lastName,
                    firstName = firstName
                )
            )) {
                is OperationResult.Success -> {
                    Token.isConfirmed =
                        result.data.isPhoneConfigured && result.data.isBirthdateConfigured
                    OperationResult.Success(
                        TokenMode(
                            isPhoneConfigured = result.data.isPhoneConfigured,
                            isBirthdateConfigured = result.data.isBirthdateConfigured,
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun fillUserInformation(
        phone: String,
        birthdate: String
    ): OperationResult<TokenMode> {
        return try {
            when (val result = service.fillUserInformation(
                UpdatePhoneAndBirthdateRequest(
                    phone = phone,
                    birthdate = birthdate
                )
            )) {
                is OperationResult.Success -> {
                    Token.isConfirmed =
                        result.data.isPhoneConfigured && result.data.isBirthdateConfigured
                    OperationResult.Success(
                        TokenMode(
                            isPhoneConfigured = result.data.isPhoneConfigured,
                            isBirthdateConfigured = result.data.isBirthdateConfigured,
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun login(email: String, password: String): OperationResult<TokenMode> {
        return try {
            when (val result = service.login(LoginRequest(email, password))) {
                is OperationResult.Success -> {
                    Token.isConfirmed =
                        result.data.isPhoneConfigured && result.data.isBirthdateConfigured
                    OperationResult.Success(
                        TokenMode(
                            isPhoneConfigured = result.data.isPhoneConfigured,
                            isBirthdateConfigured = result.data.isBirthdateConfigured,
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun google(token: String): OperationResult<TokenMode> {
        return try {
            when (val result = service.googleSignIn(GoogleTokenRequest(token))) {
                is OperationResult.Success -> {
                    Token.isConfirmed =
                        result.data.isPhoneConfigured && result.data.isBirthdateConfigured
                    OperationResult.Success(
                        TokenMode(
                            isPhoneConfigured = result.data.isPhoneConfigured,
                            isBirthdateConfigured = result.data.isBirthdateConfigured,
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun signUpUser(
        name: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        birthdate: String
    ): OperationResult<TokenMode> {
        return try {
            when (val result =
                service.signUp(
                    UserSignUpRequest(
                        name = name,
                        lastname = lastName,
                        email = email,
                        phone = phone,
                        password = password,
                        birthdate = birthdate
                    )
                )) {
                is OperationResult.Success -> {
                    Token.isConfirmed =
                        result.data.isPhoneConfigured && result.data.isBirthdateConfigured
                    OperationResult.Success(
                        TokenMode(
                            isPhoneConfigured = result.data.isPhoneConfigured,
                            isBirthdateConfigured = result.data.isBirthdateConfigured,
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }

        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun requestPasswordChangeEmail(email: String): OperationResult<String> {
        return try {
            when (val result = service.resetPasswordForEmail(RequestPasswordChangeModel(email))) {
                is OperationResult.Success -> {
                    OperationResult.Success(result.data)
                }

                is OperationResult.Error -> result
            }

        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}