package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.PaymentSuccessOrderRequest
import com.agusteam.caribeando.domain.interfaces.PaymentRepository

class ProcessSuccessPaymentOrderUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(
        order: String,
        transactionId: String,
    ): OperationResult<Boolean> {
        return repository.processOrder(
            PaymentSuccessOrderRequest(
                order,transactionId
            )
        )
    }
}