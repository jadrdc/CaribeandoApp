package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.domain.interfaces.OrderRepository
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.formatDateRange
import com.agusteam.caribeando.presenter.formatInstant
import com.agusteam.caribeando.presenter.timeUntil

class GetUpcomingTripOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(): OperationResult<List<UpcomingOrders>> {
        return when (val result = orderRepository.getUpcomingOrders()) {
            is OperationResult.Error -> result
            is OperationResult.Success -> {
                val results = result.data.map {
                    UpcomingOrders(
                        transactionId = it.id,
                        totalPayment = it.tripScheduleModel.total_payment,
                        tripImages = it.tripScheduleModel.tripModel.images,
                        timeUntilTrip = timeUntil(it.tripScheduleModel.leaving_time),
                        providerMonth = it.tripScheduleModel.tripModel.businessModel.month,
                        tripImage = it.tripScheduleModel.tripModel.images.firstOrNull() ?: "",
                        tripDestiny = it.tripScheduleModel.tripModel.destiny,
                        tripName = it.tripScheduleModel.tripModel.name,
                        providerImage = it.tripScheduleModel.tripModel.businessModel.image,
                        providerName = it.tripScheduleModel.tripModel.businessModel.name,
                        dateFrom = formatInstant(it.tripScheduleModel.leaving_time),
                        dateTo = formatInstant(it.tripScheduleModel.returning_time),
                        date = formatDateRange(
                            it.tripScheduleModel.leaving_time,
                            it.tripScheduleModel.returning_time
                        )
                    )
                }
                OperationResult.Success(results)
            }
        }
    }
}