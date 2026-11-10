package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.NetworkUnavailableException
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.model.CancelOrderRequest
import com.agusteam.caribeando.data.model.CancelOrderResponse
import com.agusteam.caribeando.data.model.PayBalanceRequest
import com.agusteam.caribeando.data.model.PayBalanceResponse
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.data.model.OrderRatingRequest
import com.agusteam.caribeando.data.model.UpcomingOrderTripModelResponse
import com.agusteam.caribeando.data.network.services.OrderService
import com.agusteam.caribeando.domain.interfaces.OrderRepository

class OrderRepositoryImp(
    private val orderService: OrderService,
    private val orderCache: OrderCache? = null
) : OrderRepository {
    override suspend fun getUpcomingOrders(): OperationResult<List<UpcomingOrderTripModelResponse>> {
        val result = try {
            orderService.getUpcomingTripOrders()
        } catch (e: Exception) {
            mapExceptions(e)
        }
        return when (result) {
            is OperationResult.Success -> {
                // Caching must never break the screen, so a storage failure is ignored.
                runCatching { orderCache?.save(result.data) }
                result
            }
            is OperationResult.Error -> {
                // Only a connectivity failure falls back to the last known orders; an auth or
                // server error must still surface.
                val cached = if (result.exception is NetworkUnavailableException) {
                    runCatching { orderCache?.load() }.getOrNull()
                } else null
                if (cached != null) OperationResult.Success(cached) else result
            }
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

    override suspend fun cancelOrder(orderId: String, reason: String?): OperationResult<CancelOrderResponse> {
        return try {
            orderService.cancelOrder(orderId, CancelOrderRequest(reason = reason))
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    override suspend fun payBalance(orderId: String, transactionId: String): OperationResult<PayBalanceResponse> {
        return try {
            orderService.payBalance(orderId, PayBalanceRequest(transactionId = transactionId))
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}
