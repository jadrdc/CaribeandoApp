package com.agusteam.caribeando.data.network

/**
 * Backend base URL. Defaults to production; a platform entry point may override it once, at
 * startup and before any request is made (Android reads it from BuildConfig, which is set with
 * `-Pcaribeando.apiBaseUrl=https://staging.example.com/`). The value must end with "/".
 */
object ApiConfig {
    const val PRODUCTION_BASE_URL = "https://caribeando-backend-production.up.railway.app/"

    var baseUrl: String = PRODUCTION_BASE_URL
}
