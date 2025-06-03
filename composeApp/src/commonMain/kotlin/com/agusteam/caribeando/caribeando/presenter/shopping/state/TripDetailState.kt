package com.agusteam.caribeando.presenter.shopping.state

import com.agusteam.caribeando.caribeando.data.model.CommentModelResponse
import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.models.TripProviderModel
import com.agusteam.caribeando.presenter.common.ItemProviderState

data class TripDetailState(
    val title: String = "",
    val galleryPhotos: List<String> = listOf(),
    val description: String = "",
    val destiny: String = "",
    val businessId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val avatarUrl: String = "",
    val isMarkedAsFavorite: Boolean = false,
    val errorModel: ErrorModel? = null,
    val tripId: String = "",
    val includedServices: List<String> = listOf(),
    val isLoading: Boolean = false,
    val isLoadingContent: Boolean = false,
    val cancellationPolicy: String = "",
    val arrivingTime: String = "",
    val leavingTime: String = "",
    val meetingPoint: String = "",
    val initialPayment: Int = 0,
    val totalPayment: Int = 0,
    val tripProviderModel: TripProviderModel? = null,
    val itemProviderState: ItemProviderState = ItemProviderState("", "", 0),
    val comments: List<CommentModelResponse> = listOf()

) : ViewModelState

