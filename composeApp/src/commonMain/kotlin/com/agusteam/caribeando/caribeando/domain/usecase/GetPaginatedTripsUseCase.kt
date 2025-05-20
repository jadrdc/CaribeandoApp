package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.TripListPaginationResponseItem
import com.agusteam.caribeando.domain.interfaces.TripRepository
import kotlinx.datetime.Instant

class GetPaginatedTripsUseCase(private val repository: TripRepository) {
    suspend fun loadMore(
        category: String = "",
        endingAmount: Int = 0,
        search: String = "",
        leavingTimeStart: Instant,
        returningTimeEnd: Instant

    ): OperationResult<List<TripListPaginationResponseItem>> {
        return repository.loadNextTripsPage(
            category = category,
            endingAmount = endingAmount,
            search = search,
            leavingTimeStart = leavingTimeStart,
            returningTimeEnd = returningTimeEnd
        )
    }

    suspend fun resetPagination() {
        repository.resetPagination()
    }

    suspend fun canLoadMore(): Boolean {
        return repository.canLoadMore()
    }
}