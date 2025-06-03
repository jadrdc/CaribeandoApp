package com.agusteam.caribeando.data.model

import com.agusteam.caribeando.domain.models.TripModel
import kotlinx.serialization.Serializable

@Serializable
data class TripWishListResponse(val tripModel: TripModel)
