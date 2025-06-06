package com.agusteam.caribeando.caribeando.domain

// iosMain
interface AppleSignInCallback {
    fun onTokenReceived(token: String)
    fun onSignInFailed(error: String)
}
