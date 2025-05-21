package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.GoogleTokenRequest
import com.agusteam.caribeando.data.model.LoginRequest
import com.agusteam.caribeando.data.model.RequestPasswordChangeModel
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


    override suspend fun login(email: String, password: String): OperationResult<TokenMode> {
        return try {
            when (val result = service.login(LoginRequest(email, password))) {
                is OperationResult.Success -> {
                    OperationResult.Success(
                        TokenMode(
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    override suspend fun google(token: String): OperationResult<TokenMode> {
        return try {
            when (val result = service.googleSignIn(GoogleTokenRequest(token))) {
                is OperationResult.Success -> {
                    OperationResult.Success(
                        TokenMode(
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    override suspend fun signUpUser(
        name: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): OperationResult<TokenMode> {
        return try {
            val today: LocalDate =
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val defaultBirthdate: String = today.minus(DatePeriod(years = 21)).toString()
            when (val result =
                service.signUp(
                    UserSignUpRequest(
                        name = name,
                        lastname = lastName,
                        email = email,
                        phone = phone,
                        password = password,
                        birthdate = defaultBirthdate
                    )
                )) {
                is OperationResult.Success -> {
                    OperationResult.Success(
                        TokenMode(
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                    )
                }

                is OperationResult.Error -> result
            }

        } catch (e: Exception) {
            OperationResult.Error(e)
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
            OperationResult.Error(e)
        }
    }
}