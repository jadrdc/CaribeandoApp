package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.TripProviderRepository
import com.agusteam.caribeando.domain.models.TripModel

class GetTripFavoriteListUseCase(val repository: TripProviderRepository) {

    suspend operator fun invoke(): OperationResult<List<TripModel>> {
        return repository.getFavoriteTripList()
    }
}