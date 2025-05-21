package com.agusteam.caribeando.data.network.services

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.ReportOrder
import com.agusteam.caribeando.data.model.TripScheduleRatingRequest
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

    suspend fun rateOrder(model: TripScheduleRatingRequest): OperationResult<Any> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}rating"
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
}