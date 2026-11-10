package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.PayBalanceResponse
import com.agusteam.caribeando.domain.interfaces.OrderRepository

/**
 * Screen 20, "Pagar faltante" (POST /order/{id}/pay-balance). Reports a Stripe transaction the
 * client already completed (via the existing generic payment-sheet flow, for the remaining
 * balance amount) as covering an already-SUCCESS order's outstanding balance -- distinct from
 * CreatePendingPaymentOrderUseCase, which creates a brand-new order from scratch.
 */
class PayOrderBalanceUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(orderId: String, transactionId: String): OperationResult<PayBalanceResponse> {
        return repository.payBalance(orderId, transactionId)
    }
}
