package com.agusteam.caribeando.data.network.services

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.CancelOrderRequest
import com.agusteam.caribeando.data.model.CancelOrderResponse
import com.agusteam.caribeando.data.model.ContactInfoResponse
import com.agusteam.caribeando.data.model.PayBalanceRequest
import com.agusteam.caribeando.data.model.PayBalanceResponse
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.data.model.OrderRatingRequest
import com.agusteam.caribeando.data.model.UpcomingOrderTripModelResponse
import com.agusteam.caribeando.presenter.URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OrderService(
    private val httpClient: HttpClient
) {

    suspend fun rateOrder(model: OrderRatingRequest): OperationResult<Boolean> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}order/rate"
            ) {
                setBody(model)
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<Boolean>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun reportOrder(model: ReportOrder): OperationResult<Boolean> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}order/report"
            ) {
                setBody(model)
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<Boolean>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun getUpcomingTripOrders(): OperationResult<List<UpcomingOrderTripModelResponse>> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}order/upcomingOrdersTrips"
            ) {

            }
            mapResponse<List<UpcomingOrderTripModelResponse>>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun getPastTrips(): OperationResult<List<UpcomingOrderTripModelResponse>> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}order/pastTrips"
            ) {

            }
            mapResponse<List<UpcomingOrderTripModelResponse>>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    /**
     * Real cancellation flow — POST /order/{id}/cancel on the backend. Distinct from
     * PaymentService.cancelOrder(), which posts to /payment/order-failure and is for a payment
     * attempt that failed before charging, not for cancelling an already-paid booking.
     */
    suspend fun cancelOrder(orderId: String, request: CancelOrderRequest): OperationResult<CancelOrderResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}order/${orderId}/cancel"
            ) {
                setBody(request)
                contentType(ContentType.Application.Json)
            }
            mapResponse<CancelOrderResponse>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    /**
     * Screen 20, "Pagar faltante" — POST /order/{id}/pay-balance. Reports a Stripe transaction
     * (already completed client-side via the existing generic payment-sheet flow) as covering
     * an existing SUCCESS order's remaining balance.
     */
    suspend fun payBalance(orderId: String, request: PayBalanceRequest): OperationResult<PayBalanceResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}order/${orderId}/pay-balance"
            ) {
                setBody(request)
                contentType(ContentType.Application.Json)
            }
            mapResponse<PayBalanceResponse>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    /**
     * 2026-10-09: GET /support/contact -- support phone/WhatsApp/email for "Contáctanos".
     * Lives here rather than in a new service so no new DI registration is needed.
     */
    suspend fun getContactInfo(): OperationResult<ContactInfoResponse> {
        return try {
            val response = httpClient.get(urlString = "${URL}support/contact")
            mapResponse<ContactInfoResponse>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}
