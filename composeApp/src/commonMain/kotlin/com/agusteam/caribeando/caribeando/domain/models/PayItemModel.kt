package com.agusteam.caribeando.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PayItemModel(
    val title: String,
    val destiny: String,
    val profilePhoto: String,
    val leavingTime: String,
    val meetingPoint: String,
    val initialPayment: Int,
    val totalPayment: Int,
    val tripSchedule: String,
    val arrivingTime: String,
    val businessName: String,
    val businessPhoto: String,
    val businessMonth: String,
    val galleryPhoto: List<String> = listOf()
)
