package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.TripRepository

class GetTripsIncludedServicesUseCase(val repository: TripRepository) {

    suspend operator fun invoke(tripId: String): OperationResult<List<String>> {
       return repository.getTripsIncludedServices(tripId)
    }
}