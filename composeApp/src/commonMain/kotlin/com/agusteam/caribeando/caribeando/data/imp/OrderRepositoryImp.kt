package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.data.model.OrderRatingRequest
import com.agusteam.caribeando.data.model.UpcomingOrderTripModelResponse
import com.agusteam.caribeando.data.network.services.OrderService
import com.agusteam.caribeando.domain.interfaces.OrderRepository

class OrderRepositoryImp(private val orderService: OrderService) : OrderRepository {
    override suspend fun getUpcomingOrders(): OperationResult<List<UpcomingOrderTripModelResponse>> {
        return try {
            val orders = orderService.getUpcomingTripOrders()
            orders
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun rateOrder(
        rating: Double,
        orderId: String,
        comment: String
    ): OperationResult<Any> {
        return try {
            val orders = orderService.rateOrder(
                OrderRatingRequest(
                    rating = rating,
                    comment = comment,
                    orderId = orderId
                )
            )
            orders
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun getPastTrips(): OperationResult<List<UpcomingOrderTripModelResponse>> {
        return try {
            val orders = orderService.getPastTrips()
            orders
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun reportOrder(model: ReportOrder): OperationResult<Boolean> {
        return try {
            val result = orderService.reportOrder(model)
            result
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}