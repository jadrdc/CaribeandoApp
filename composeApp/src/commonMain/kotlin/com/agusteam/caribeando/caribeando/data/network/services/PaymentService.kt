package com.agusteam.caribeando.data.network.services

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.PaymentFailureRequest
import com.agusteam.caribeando.data.model.PaymentPendingOrderRequest
import com.agusteam.caribeando.data.model.PaymentPendingOrderResponse
import com.agusteam.caribeando.data.model.PaymentSuccessOrderRequest
import com.agusteam.caribeando.data.model.StripePaymentIntentRequest
import com.agusteam.caribeando.data.model.StripePaymentIntentResponse
import com.agusteam.caribeando.data.model.TripFavoriteRequest
import com.agusteam.caribeando.presenter.URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PaymentService(
    private val httpClient: HttpClient
) {

    suspend fun getStripeIntent(model: StripePaymentIntentRequest): OperationResult<StripePaymentIntentResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}payment/payment-sheet"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(model)
            }
            mapResponse<StripePaymentIntentResponse>(response)

        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun createPendingOrder(request: PaymentPendingOrderRequest): OperationResult<PaymentPendingOrderResponse> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}payment/pending-order"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(request)
            }
            val res = mapResponse<PaymentPendingOrderResponse>(response)
            res
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun processOrder(request: PaymentSuccessOrderRequest): OperationResult<Boolean> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}payment/order-sucess"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(request)
            }
            mapResponse<Boolean>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun cancelOrder(request: PaymentFailureRequest): OperationResult<Boolean> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}payment/order-failure"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
                setBody(request)
            }
            mapResponse<Boolean>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun removeOrder(id: String): OperationResult<String> {
        return try {
            val response = httpClient.delete(
                urlString = "${URL}payment/${id}"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<String>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}