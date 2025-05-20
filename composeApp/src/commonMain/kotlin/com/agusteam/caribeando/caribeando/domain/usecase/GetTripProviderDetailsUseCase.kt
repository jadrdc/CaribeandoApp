package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.TripProviderRepository
import com.agusteam.caribeando.domain.models.TripProviderModel

class GetTripProviderDetailsUseCase(private val repository: TripProviderRepository) {
    suspend operator fun invoke(id:String): OperationResult<TripProviderModel> {
        return repository.getTripProviderDetails(id)
    }
}