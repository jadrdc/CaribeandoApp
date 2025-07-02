package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.PaymentPendingOrderRequest
import com.agusteam.caribeando.data.model.PaymentPendingOrderResponse
import com.agusteam.caribeando.domain.interfaces.PaymentRepository

class CreatePendingPaymentOrderUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(
        tripscheduleId: String,
        amount: Double
    ): OperationResult<PaymentPendingOrderResponse> {
        return repository.createPendingOrder(
            PaymentPendingOrderRequest(
                tripscheduleId, amount
            )
        )
    }
}