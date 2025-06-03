package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.toDomain
import com.agusteam.caribeando.data.network.services.TripProviderService
import com.agusteam.caribeando.domain.interfaces.TripProviderRepository
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.domain.models.TripProviderModel

class TripProviderRepositoryImp(private val service: TripProviderService) : TripProviderRepository {
    override suspend fun getTripProviderDetails(id: String): OperationResult<TripProviderModel> {
        return try {
            when (val result = service.getTripProviderInformation(id)) {
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

    override suspend fun getUpcomingTripsByProvider(id: String): OperationResult<List<TripModel>> {
        return try {
            when (val result = service.getUpcomingTripsByProvider(id)) {
                is OperationResult.Success -> {
                    OperationResult.Success(result.data.toDomain())
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    override suspend fun getFavoriteTripList(): OperationResult<List<TripModel>> {
        return try {
            when (val result = service.getFavoriteTripList()) {
                is OperationResult.Success -> {

                    OperationResult.Success(result.data.map { it.toDomain() })
                }

                is OperationResult.Error -> result
            }
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }
}