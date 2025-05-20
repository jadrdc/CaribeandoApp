package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.PaymentFailureRequest
import com.agusteam.caribeando.domain.interfaces.PaymentRepository

class CancelPaymentOrderUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(
        order: String,
        reason: String
    ): OperationResult<Boolean> {
        return repository.cancelOrder(
            PaymentFailureRequest(
                orderId = order, reason = reason
            )
        )
    }
}