package com.agusteam.caribeando.presenter.social

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.auth.GoogleAuthUiClient
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.presenter.social.state.SocialSignState
import kotlinx.coroutines.launch

class SocialSignViewModel(private val googleServiceProvider: GoogleAuthUiClient) :
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
                        val w = e.message
                    }
                }

                else -> {

                }
            }

        }
    }

    fun onSignInResult(data: Intent) {
        viewModelScope.launch {
            try {
                val signInResult = googleServiceProvider.signInWithIntent(data)
                // Maneja el resultado aquí
            } catch (e: Exception) {

            }
        }
    }


}