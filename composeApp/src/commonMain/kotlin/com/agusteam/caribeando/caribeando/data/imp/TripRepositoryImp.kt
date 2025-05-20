package com.agusteam.caribeando.data.imp

import PaginationManager
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.TripFavoriteRequest
import com.agusteam.caribeando.data.model.TripListPaginationResponseItem
import com.agusteam.caribeando.data.model.TripsAvailablePaginationRequest
import com.agusteam.caribeando.data.network.services.TripService
import com.agusteam.caribeando.domain.interfaces.TripRepository
import kotlinx.datetime.Instant

class TripRepositoryImp(private val service: TripService) : TripRepository {
    private val paginationManager = PaginationManager<TripListPaginationResponseItem>()

    override suspend fun canLoadMore(): Boolean {
        return paginationManager.canLoadMore()
    }

    override suspend fun resetPagination() {
        paginationManager.reset()
    }

    override suspend fun loadNextTripsPage(
        category: String,
        endingAmount: Int,
        search: String,
        leavingTimeStart: Instant,
        returningTimeEnd: Instant
    ): OperationResult<List<TripListPaginationResponseItem>> {
        return try {
            paginationManager.loadNextPage { page ->
                println("CRUSEL 00 $page")

                // You may need to adjust the request to include the page number
                val result = service.getTrips(
                    TripsAvailablePaginationRequest(
                        amount = endingAmount,
                        category = category,
                        search = search,
                        leavingTimeStart = leavingTimeStart,
                        returningTimeEnd = returningTimeEnd,
                        page = page // <-- Make sure your request and service support this!
                    )
                )
                when (result) {
                    is OperationResult.Success -> result.data
                    is OperationResult.Error -> throw result.exception
                }
            }.fold(
                onSuccess = { OperationResult.Success(it) },
                onFailure = { OperationResult.Error(Exception(it.message)) }
            )
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }


    override suspend fun getTripsIncludedServices(tripId: String): OperationResult<List<String>> {
        return try {
            when (val result = service.getTripsIncludeServices(tripId)) {
                is OperationResult.Success -> {
                    val model = result.data
                    OperationResult.Success(model)
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    override suspend fun markFavorite(tripId: String): OperationResult<String> {
        return try {
            when (val result =
                service.markAsFavorite(TripFavoriteRequest(tripId))) {
                is OperationResult.Success -> {
                    OperationResult.Success(result.data)
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    override suspend fun unmarkAsFavorite(
        tripId: String
    ): OperationResult<String> {
        return try {
            when (val result =
                service.unmarkAsFavorite(
                    TripFavoriteRequest(
                        tripId
                    )
                )) {
                is OperationResult.Success -> {
                    OperationResult.Success(result.data)
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }
}