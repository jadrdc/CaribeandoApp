package com.agusteam.caribeando.data.mappers

import coil3.network.HttpException
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.CategoryResponse
import com.agusteam.caribeando.data.model.TripListPaginationResponseItem
import com.agusteam.caribeando.data.model.TripProviderUpcomingTripsResponseItem
import com.agusteam.caribeando.domain.models.CategoryModel
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.domain.models.UpcomingOrders
import com.agusteam.caribeando.presenter.formatDateRange
import com.agusteam.caribeando.presenter.formatInstant
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException

fun UpcomingOrders.toDomain(): TripModel {
    return TripModel(
        tripScheduleId = scheduledId,
        transactionId = transactionId,
        id = "",
        name = tripName,
        images = listOf(tripImage),
        date = date,
        hasBeenEvaluated = hasBeenEvaluated
    )
}

fun TripListPaginationResponseItem.toDomain(): TripModel {
    return TripModel(
        reviewCount = reviewCount,
        rating = rating,
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
            val errorMessage = getErrorCode(response.status.value)
            OperationResult.Error(Exception(errorMessage))
        }
    }
}

fun mapExceptions(e: Exception): OperationResult<Nothing> {
    return when (e) {
        is IOException -> OperationResult.Error(Exception("Error de conexión: No se pudo conectar al servidor"))
        is HttpException -> {
            val mensajeError = getErrorCode(e.response.code)
            OperationResult.Error(Exception(mensajeError))
        }

        else -> OperationResult.Error(Exception("Error inesperado: ${e.message ?: e.message ?: "Error desconocido"}"))
    }
}

fun getErrorCode(
    code: Int
): String {
    return when (code) {
        400 -> "Solicitud incorrecta: Los datos enviados son inválidos"
        401 -> "No autorizado: Credenciales incorrectas"
        403 -> "Prohibido: No tienes permisos para esta acción"
        404 -> "No encontrado: El recurso solicitado no existe"
        500 -> "Error del servidor: Problema interno del servidor"
        else -> "Error HTTP ${code}: Error desconocido"
    }

}