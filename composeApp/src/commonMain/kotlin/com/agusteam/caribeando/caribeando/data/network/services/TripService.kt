package com.agusteam.caribeando.data.network.services

import com.agusteam.caribeando.caribeando.data.model.CommentModelResponse
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.PaginationDTO
import com.agusteam.caribeando.data.model.TripDetailsBodyDTO
import com.agusteam.caribeando.data.model.TripFavoriteRequest
import com.agusteam.caribeando.data.model.TripListPaginationResponseItem
import com.agusteam.caribeando.data.model.TripsAvailablePaginationRequest
import com.agusteam.caribeando.presenter.URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TripService(
    private val httpClient: HttpClient
) {

    suspend fun getComments(tripId: String): OperationResult<List<CommentModelResponse>> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}trip/comments/$tripId" // Use string interpolation to insert the trip
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<List<CommentModelResponse>>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun markAsFavorite(model: TripFavoriteRequest): OperationResult<String> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}trip/favorite"
            ) {
                setBody(model)
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<String>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun getTripsIncludeServices(trip: String): OperationResult<List<String>> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}trip/included/$trip" // Use string interpolation to insert the trip
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<List<String>>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun unmarkAsFavorite(model: TripFavoriteRequest): OperationResult<String> {
        return try {
            val response = httpClient.delete(
                urlString = "${URL}trip/favorite/${model.trip}"
            ) {
                setBody(model)
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<String>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun getTrips(req: TripsAvailablePaginationRequest): OperationResult<PaginationDTO<TripListPaginationResponseItem>> {
        return try {
            val response = httpClient.post(
                urlString = "${URL}trip/available"
            ) {
                setBody(req)
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<PaginationDTO<TripListPaginationResponseItem>>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun getImages(tripId: String): OperationResult<List<String>> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}trip/image/$tripId"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<List<String>>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

    suspend fun getTripDetails(tripId: String): OperationResult<TripDetailsBodyDTO> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}trip/details/$tripId"
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<TripDetailsBodyDTO>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }
}