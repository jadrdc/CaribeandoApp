package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.CancelOrderResponse
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.data.model.UpcomingOrderTripModelResponse
import com.agusteam.caribeando.domain.interfaces.OrderRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Records the orderId/reason it was called with rather than hitting a real network, so this
 * verifies RequestOrderCancellationUseCase's contract (does it pass the right arguments through,
 * does it hand back OperationResult unchanged) without depending on OrderService/Ktor.
 */
private class FakeOrderRepository(
    private val cancelOrderResult: OperationResult<CancelOrderResponse>
) : OrderRepository {

    var lastCancelOrderId: String? = null
    var lastCancelReason: String? = null

    override suspend fun getUpcomingOrders(): OperationResult<List<UpcomingOrderTripModelResponse>> =
        throw NotImplementedError("not used by this test")

    override suspend fun getPastTrips(): OperationResult<List<UpcomingOrderTripModelResponse>> =
        throw NotImplementedError("not used by this test")

    override suspend fun reportOrder(model: ReportOrder): OperationResult<Boolean> =
        throw NotImplementedError("not used by this test")

    override suspend fun rateOrder(rating: Double, orderId: String, comment: String): OperationResult<Any> =
        throw NotImplementedError("not used by this test")

    override suspend fun cancelOrder(orderId: String, reason: String?): OperationResult<CancelOrderResponse> {
        lastCancelOrderId = orderId
        lastCancelReason = reason
        return cancelOrderResult
    }
}

class RequestOrderCancellationUseCaseTest {

    @Test
    fun `returns the repository's success result and forwards orderId and reason unchanged`() = runBlocking {
        val expected = CancelOrderResponse(
            status = "refund_pending",
            refundEligible = true,
            message = "Your booking was cancelled. A refund is pending."
        )
        val fakeRepository = FakeOrderRepository(cancelOrderResult = OperationResult.Success(expected))
        val useCase = RequestOrderCancellationUseCase(fakeRepository)

        val result = useCase(orderId = "order-123", reason = "Change of plans")

        assertTrue(result is OperationResult.Success)
        assertEquals(expected, (result as OperationResult.Success).data)
        assertEquals("order-123", fakeRepository.lastCancelOrderId)
        assertEquals("Change of plans", fakeRepository.lastCancelReason)
    }

    @Test
    fun `defaults reason to null when the caller doesn't supply one`() = runBlocking {
        val fakeRepository = FakeOrderRepository(
            cancelOrderResult = OperationResult.Success(
                CancelOrderResponse(status = "cancelled_by_traveler", refundEligible = false, message = "Cancelled.")
            )
        )
        val useCase = RequestOrderCancellationUseCase(fakeRepository)

        useCase(orderId = "order-456")

        assertEquals("order-456", fakeRepository.lastCancelOrderId)
        assertEquals(null, fakeRepository.lastCancelReason)
    }

    @Test
    fun `propagates an error result from the repository instead of throwing`() = runBlocking {
        val failure = OperationResult.Error(IllegalStateException("network down"))
        val fakeRepository = FakeOrderRepository(cancelOrderResult = failure)
        val useCase = RequestOrderCancellationUseCase(fakeRepository)

        val result = useCase(orderId = "order-789")

        assertFalse(result is OperationResult.Success)
        assertTrue(result is OperationResult.Error)
        assertEquals("network down", (result as OperationResult.Error).exception.message)
    }
}
