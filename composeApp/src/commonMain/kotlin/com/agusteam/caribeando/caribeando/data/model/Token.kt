package com.agusteam.caribeando.data.model

object Token {
    var isInformationLoaded: Boolean = false
    var token: String = ""
    var refreshToken: String = ""
    val isValid: Boolean
        get() = token.isNotBlank() && refreshToken.isNotBlank()

    fun logout() {
        refreshToken = ""
        token = ""
        isInformationLoaded = false
    }
}