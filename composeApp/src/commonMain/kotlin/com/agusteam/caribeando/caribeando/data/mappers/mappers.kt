package com.agusteam.caribeando.data.mappers

import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.CategoryResponse
import com.agusteam.caribeando.data.model.ErrorResponse
import com.agusteam.caribeando.data.model.TripListPaginationResponseItem
import com.agusteam.caribeando.data.model.TripProviderUpcomingTripsResponseItem
import com.agusteam.caribeando.data.model.TripWishListResponse
import com.agusteam.caribeando.domain.models.CategoryModel
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.formatDateRange
import com.agusteam.caribeando.presenter.formatInstant
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.InternalAPI

fun UpcomingOrders.toDomain(): TripModel {
    return TripModel(id = "", name = tripName, images = listOf(tripImage), date = date)
}

fun TripListPaginationResponseItem.toDomain(): TripModel {
    return TripModel(
        cancellation_policy = cancellation_policy,
        id = id,
        businessId = businessModel.id,
        businessName = businessModel.name,
        images = images,
        businessImage = businessModel.image,
        name = name,
        description = description,
        lat = lat.toDouble(),
        lng = lng.toDouble(),
        date = formatDateRange(
            start = details.leaving_time,
            end = details.returning_time
        ),
        isSavedForLater = isFavorite,
        tripScheduleId = details.id,
        destiny = destiny,
        month = businessModel.month,
        categoryList = listOf(),
        initialPayment = details.initial_payment,
        meetingPoint = details.meeting_point,
        price = details.total_payment,
        leavingTime = formatInstant(details.leaving_time),
        arrivingTime = formatInstant(
            details.returning_time
        )
    )
}

fun List<TripProviderUpcomingTripsResponseItem>.toDomain(): List<TripModel> {
    return map {
        TripModel(
            date = formatInstant(it.leaving_time),
            id = it.tripModel.id,
            name = it.tripModel.name,
            images = it.tripModel.images,
            destiny = it.tripModel.destiny,
            arrivingTime = it.returning_time,
            leavingTime = formatInstant(it.leaving_time),
            price = it.total_payment
        )
    }
}

fun CategoryResponse.toDomainModel(): CategoryModel {
    return CategoryModel(
        id = id,
        description = description, isSelected = false, image = image
    )
}

suspend inline fun <reified T> mapResponse(response: HttpResponse): OperationResult<T> {
    return when (response.status.value) {
        in 200..299 -> {
            // Map the response body to the expected type
            val body = response.body<T>()
            OperationResult.Success(body)
        }

        else -> {
            // Handle error response and map it to the ErrorResponse type
            println("CRUSEL ${response.bodyAsText()}")
            val error = response.body<ErrorResponse>()
            OperationResult.Error(Exception(error.error))
        }
    }
}
