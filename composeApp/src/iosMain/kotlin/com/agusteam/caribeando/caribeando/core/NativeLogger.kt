package com.agusteam.caribeando.caribeando.core

interface NativeLogger{
    fun log(message: String)
    fun recordException(throwable: Throwable)
    fun forceCrash()
}

