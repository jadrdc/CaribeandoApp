package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.PaymentFailureRequest
import com.agusteam.caribeando.data.model.PaymentPendingOrderRequest
import com.agusteam.caribeando.data.model.PaymentPendingOrderResponse
import com.agusteam.caribeando.data.model.PaymentSuccessOrderRequest
import com.agusteam.caribeando.data.model.StripePaymentIntentRequest
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse

interface PaymentRepository {
    suspend fun getStripeIntent(model: StripePaymentIntentRequest): OperationResult<StripePaymentIntentResponse>
    suspend fun createPendingOrder(req: PaymentPendingOrderRequest): OperationResult<PaymentPendingOrderResponse>
    suspend fun processOrder(req: PaymentSuccessOrderRequest): OperationResult<Boolean>
    suspend fun cancelOrder(req: PaymentFailureRequest): OperationResult<Boolean>
    suspend fun removeOrder(req: String): OperationResult<String>
}