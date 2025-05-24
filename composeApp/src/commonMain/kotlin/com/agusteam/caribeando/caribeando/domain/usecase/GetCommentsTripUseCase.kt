package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.caribeando.data.model.CommentModelResponse
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.TripProviderRepository
import com.agusteam.caribeando.domain.interfaces.TripRepository
import com.agusteam.caribeando.domain.models.TripModel

class GetCommentsTripUseCase(val repository: TripRepository) {

    suspend operator fun invoke(tripId: String): OperationResult<List<CommentModelResponse>> {
        return repository.getComments(tripId)
    }
}