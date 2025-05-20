package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.PaymentFailureRequest
import com.agusteam.caribeando.data.model.PaymentPendingOrderRequest
import com.agusteam.caribeando.data.model.PaymentPendingOrderResponse
import com.agusteam.caribeando.data.model.PaymentSuccessOrderRequest
import com.agusteam.caribeando.data.model.StripePaymentIntentRequest
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse
import com.agusteam.caribeando.data.network.services.PaymentService
import com.agusteam.caribeando.domain.interfaces.PaymentRepository

class PaymentRepositoryImp(private val service: PaymentService) : PaymentRepository {

    override suspend fun processOrder(req: PaymentSuccessOrderRequest): OperationResult<Boolean> {
        return service.processOrder(req)
    }

    override suspend fun getStripeIntent(model: StripePaymentIntentRequest): OperationResult<StripePaymentIntentResponse> {
        return service.getStripeIntent(model)
    }

    override suspend fun createPendingOrder(req: PaymentPendingOrderRequest): OperationResult<PaymentPendingOrderResponse> {
        return service.createPendingOrder(req)
    }

    override suspend fun cancelOrder(req: PaymentFailureRequest): OperationResult<Boolean> {
        return service.cancelOrder(req)
    }
}