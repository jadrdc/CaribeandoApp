package com.agusteam.caribeando.domain.interfaces

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.domain.models.TripProviderModel

interface TripProviderRepository {
    suspend fun getTripProviderDetails(id: String): OperationResult<TripProviderModel>
    suspend fun getUpcomingTripsByProvider(id: String): OperationResult<List<TripModel>>
    suspend fun getFavoriteTripList(): OperationResult<List<TripModel>>
}