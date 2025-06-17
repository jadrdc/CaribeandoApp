package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.caribeando.data.model.CommentModelResponse
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.TripListPaginationResponseItem
import kotlinx.datetime.Instant

interface TripRepository {
    suspend fun getComments(tripId: String): OperationResult<List<CommentModelResponse>>
    suspend fun markFavorite(tripId: String): OperationResult<String>
    suspend fun getTripsIncludedServices(tripId: String): OperationResult<List<String>>
    suspend fun unmarkAsFavorite(tripId: String): OperationResult<String>
    suspend fun loadNextTripsPage(
        category: String,
        endingAmount: Int,
        search: String,
        leavingTimeStart: Instant,
        returningTimeEnd: Instant
    ): OperationResult<List<TripListPaginationResponseItem>>

    suspend fun resetPagination()
    suspend fun canLoadMore(): Boolean
    suspend fun getImages(tripId: String): OperationResult<List<String>>
}