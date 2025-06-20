package com.agusteam.caribeando.caribeando.core

interface StripeNativeBridge {
    fun configure(
        publishableKey: String,
        customerId: String,
        ephemeralKey: String,
        paymentIntent: String
    )
    fun presentSheet()
}