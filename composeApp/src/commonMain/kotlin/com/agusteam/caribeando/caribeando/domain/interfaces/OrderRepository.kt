package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.data.model.UpcomingOrderTripModelResponse

interface OrderRepository {
    suspend fun getUpcomingOrders(): OperationResult<List<UpcomingOrderTripModelResponse>>
    suspend fun getPastTrips(): OperationResult<List<UpcomingOrderTripModelResponse>>
    suspend fun reportOrder(model: ReportOrder): OperationResult<Boolean>
    suspend fun rateOrder(
        rating: Double,
        orderId: String,
        comment: String
    ): OperationResult<Any>
}