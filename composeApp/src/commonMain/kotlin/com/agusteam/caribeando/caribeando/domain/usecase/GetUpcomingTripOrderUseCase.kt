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
                        tripName = it.tripScheduleModel.tripModel.name,
                        tripDestiny = it.tripScheduleModel.tripModel.destiny,
                        tripImage = it.tripScheduleModel.tripModel.images.firstOrNull() ?: "",
                        providerName = it.tripScheduleModel.tripModel.businessModel.name,
                        providerImage = it.tripScheduleModel.tripModel.businessModel.image,
                        date = formatDateRange(
                            it.tripScheduleModel.leaving_time,
                            it.tripScheduleModel.returning_time
                        ),
                        providerMonth = it.tripScheduleModel.tripModel.businessModel.month,
                        timeUntilTrip = timeUntil(it.tripScheduleModel.leaving_time),
                        tripImages = it.tripScheduleModel.tripModel.images,
                        dateFrom = formatInstant(it.tripScheduleModel.leaving_time),
                        dateTo = formatInstant(it.tripScheduleModel.returning_time),
                        totalPayment = it.tripScheduleModel.total_payment,
                        scheduledId = it.scheduledId
                    )
                }
                OperationResult.Success(results)
            }
        }
    }
}