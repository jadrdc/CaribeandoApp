package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.TripDetailsBodyDTO
import com.agusteam.caribeando.domain.interfaces.TripRepository

class GetTripDetailsUseCase(val repository: TripRepository) {

    suspend operator fun invoke(tripId: String): OperationResult<TripDetailsBodyDTO> {
        return repository.getTripDetails(tripId)
    }
}