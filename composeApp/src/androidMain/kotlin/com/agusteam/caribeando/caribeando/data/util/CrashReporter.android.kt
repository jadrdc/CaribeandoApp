package com.agusteam.caribeando.caribeando.data.util

// commonMain
actual class CrashReporter {
    actual fun log(message: String) {
        com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().log(message)
    }

    actual fun recordException(throwable: Throwable) {
        com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    actual fun forceCrash() {
        throw RuntimeException("Forced crash for testing Crashlytics (Android)")
    }
}