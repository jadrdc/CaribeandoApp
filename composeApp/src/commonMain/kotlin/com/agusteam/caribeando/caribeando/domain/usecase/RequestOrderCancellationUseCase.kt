package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.CancelOrderResponse
import com.agusteam.caribeando.domain.interfaces.OrderRepository

/**
 * Cancels an already-placed booking (POST /order/{id}/cancel), distinct from
 * CancelPaymentOrderUseCase which is for a payment attempt that failed before ever charging the
 * customer.
 */
class RequestOrderCancellationUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(orderId: String, reason: String? = null): OperationResult<CancelOrderResponse> {
        return repository.cancelOrder(orderId, reason)
    }
}
