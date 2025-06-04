package com.agusteam.caribeando.presenter.social

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.auth.GoogleAuthUiClient
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.usecase.GoogleSignInUseCase
import com.agusteam.caribeando.domain.usecase.SaveTokenDataUseCase
import com.agusteam.caribeando.presenter.social.state.SocialSignState
import kotlinx.coroutines.launch

class SocialSignViewModel(
    private val logger:CrashReporter,
    private val googleServiceProvider: GoogleAuthUiClient,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val saveTokenDataUseCase: SaveTokenDataUseCase,

    ) :
    GenericViewModel<SocialSignState, SocialSignInEvent>(SocialSignState()) {


    fun handleEvent(event: SocialSignInEvent) {
        viewModelScope.launch {
            when (event) {
                is SocialSignInEvent.Login -> {
                    try {
                        val signInIntentSender = googleServiceProvider.signIn()
                        event.launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    } catch (e: Exception) {
                        e.message
                    }
                }

                is SocialSignInEvent.SignInSuccessful -> {
                    onSignInResult(event.data)
                }

                else -> {

                }
            }

        }
    }

    private fun onSignInResult(data: Intent) {
        viewModelScope.launch {
            try {
                val signInResult = googleServiceProvider.signInWithIntent(data)
                val result = googleSignInUseCase(signInResult.data?.googleIdToken ?: "")
                when (result) {
                    is OperationResult.Error -> {
                        logger.recordException(result.exception)
                    }

                    is OperationResult.Success -> {
                        saveTokenDataUseCase(result.data)
                        sendEvent(SocialSignInEvent.Success(result.data))
                    }
                }

                // Maneja el resultado aquí
            } catch (_: Exception) {

            }
        }
    }

}