package com.agusteam.caribeando.caribeando.data.util

// commonMain
expect class CrashReporter {
    fun log(message: String)
    fun recordException(throwable: Throwable)
    fun forceCrash()
}
