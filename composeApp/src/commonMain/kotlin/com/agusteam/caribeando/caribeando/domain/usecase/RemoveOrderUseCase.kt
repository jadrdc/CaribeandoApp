package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.PaymentFailureRequest
import com.agusteam.caribeando.domain.interfaces.PaymentRepository

class RemoveOrderUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(
        order: String,
    ): OperationResult<String> {
        return repository.removeOrder(
            order
        )
    }
}