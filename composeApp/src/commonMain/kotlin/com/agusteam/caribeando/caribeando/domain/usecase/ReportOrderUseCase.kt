package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.domain.interfaces.OrderRepository

class ReportOrderUseCase(val repository: OrderRepository) {
    suspend operator fun invoke(orderId: String, message: String): OperationResult<Boolean> {
        return repository.reportOrder(ReportOrder(order = orderId, message = message))
    }
}

