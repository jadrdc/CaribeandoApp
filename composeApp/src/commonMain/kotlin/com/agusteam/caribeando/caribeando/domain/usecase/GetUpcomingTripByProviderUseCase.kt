package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.TripProviderRepository
import com.agusteam.caribeando.domain.models.TripModel

class GetUpcomingTripByProviderUseCase(val repository: TripProviderRepository) {

    suspend operator fun invoke(providerId: String): OperationResult<List<TripModel>> {
        return repository.getUpcomingTripsByProvider(providerId)
    }
}