package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.StripePaymentIntentRequest
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse
import com.agusteam.caribeando.domain.interfaces.PaymentRepository

class GetStripePaymentIntentUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(amount: Double,description:String): OperationResult<StripePaymentIntentResponse> {
        return repository.getStripeIntent(StripePaymentIntentRequest(amount,description))
    }
}