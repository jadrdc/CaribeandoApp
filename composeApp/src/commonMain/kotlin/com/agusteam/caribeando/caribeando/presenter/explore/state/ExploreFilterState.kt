package com.agusteam.caribeando.presenter.explore.state

import com.agusteam.caribeando.domain.models.CategoryModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class ExploreFilterState(
    val selectedCategoryModel: CategoryModel?,
    val searchText: String = "",
    val currentSearch: String = "",
    val minimumAmount: Float = 1000f,
    val maximumAmount: Float = 13000f,
    val currentAmount: Float = 13000f,
    val selectedAmount: Float = 13000f,

    val leavingTimeStart: Instant = Clock.System.now(),
    val returningTimeEnd: Instant = LocalDateTime(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(
            3,
            DateTimeUnit.MONTH
        ),
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    ).toInstant(TimeZone.currentSystemDefault()),


    val selectedLeavingTimeStart: Instant = Clock.System.now(),
    val selectedReturningTimeEnd: Instant = LocalDateTime(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(
            3,
            DateTimeUnit.MONTH
        ),
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    ).toInstant(TimeZone.currentSystemDefault())
)
