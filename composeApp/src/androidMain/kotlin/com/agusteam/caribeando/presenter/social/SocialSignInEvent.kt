package com.agusteam.caribeando.presenter.social

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest

sealed interface SocialSignInEvent {
    data object Success : SocialSignInEvent
    data class Failed(val message: String) : SocialSignInEvent
    class Login(val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) :
        SocialSignInEvent

    data class SignInSuccessful(val data: Intent) : SocialSignInEvent
}