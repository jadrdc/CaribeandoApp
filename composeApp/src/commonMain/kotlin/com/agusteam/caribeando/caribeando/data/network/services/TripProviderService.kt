package com.agusteam.caribeando.data.network.services


import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.mapExceptions
import com.agusteam.caribeando.data.mappers.mapResponse
import com.agusteam.caribeando.data.model.TripListPaginationResponseItem
import com.agusteam.caribeando.data.model.TripProviderUpcomingTripsResponseItem
import com.agusteam.caribeando.domain.models.TripProviderModel
import com.agusteam.caribeando.presenter.URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TripProviderService(
    private val httpClient: HttpClient
) {
    suspend fun getUpcomingTripsByProvider(providerId: String): OperationResult<List<TripProviderUpcomingTripsResponseItem>> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}provider/getBusinessUpcomingTrips/${providerId}" // Use string interpolation to insert the trip
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<List<TripProviderUpcomingTripsResponseItem>>(response)
        } catch (e: Exception) {
             mapExceptions(e)
        }
    }

    suspend fun getFavoriteTripList(): OperationResult<List<TripListPaginationResponseItem>> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}trip/favorite" // Use string interpolation to insert the trip
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<List<TripListPaginationResponseItem>>(response)
        } catch (e: Exception) {
             mapExceptions(e)
        }
    }


    suspend fun getTripProviderInformation(business: String): OperationResult<TripProviderModel> {
        return try {
            val response = httpClient.get(
                urlString = "${URL}provider/information/${business}" // Use string interpolation to insert the trip
            ) {
                contentType(ContentType.Application.Json) // Ensure the Content-Type is set
            }
            mapResponse<TripProviderModel>(response)
        } catch (e: Exception) {
            mapExceptions(e)
        }
    }

}