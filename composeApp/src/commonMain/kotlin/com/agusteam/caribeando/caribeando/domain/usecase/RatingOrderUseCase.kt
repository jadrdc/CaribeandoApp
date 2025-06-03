package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.domain.interfaces.OrderRepository

class RatingOrderUseCase(val repository: OrderRepository) {
    suspend operator fun invoke(
        rating: Int,
        orderId: String,
        comment: String
    ): OperationResult<Any> {
        return repository.rateOrder(rating = rating.toDouble(), orderId = orderId, comment)
    }
}