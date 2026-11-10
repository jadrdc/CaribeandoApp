package com.agusteam.caribeando.core.base

/** The server could not be reached (no connection, timeout). Lets callers fall back to cached data. */
class NetworkUnavailableException(message: String) : Exception(message)
