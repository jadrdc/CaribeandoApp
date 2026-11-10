package com.agusteam.caribeando.presenter.explore.state

import com.agusteam.caribeando.domain.models.CategoryModel
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
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
    ).toInstant(TimeZone.currentSystemDefault()),

    // 2026-10-09 (design handoff, screens 7-8): date filter modes. A single-day chip
    // (`selectedDateChip`) and an exact range picked on the Calendario screen
    // (`selectedHasExactRange`) are mutually exclusive -- setting one clears the other; both
    // unset means "Cualquier fecha". The `selected*` values are the pending choice inside the
    // filter sheet; the `current*` values are what was last applied (mirrors the existing
    // selectedAmount/currentAmount pair).
    val selectedDateChip: LocalDate? = null,
    val selectedHasExactRange: Boolean = false,
    val currentDateChip: LocalDate? = null,
    val currentHasExactRange: Boolean = false,
) {
    /** True when the sheet's pending choices differ from the last applied filter. */
    fun hasPendingChanges(appliedCategoryId: String?): Boolean =
        (selectedCategoryModel?.id ?: "") != (appliedCategoryId ?: "") ||
            searchText != currentSearch ||
            selectedAmount != currentAmount ||
            selectedDateChip != currentDateChip ||
            selectedHasExactRange != currentHasExactRange ||
            (selectedHasExactRange && (selectedLeavingTimeStart != leavingTimeStart ||
                selectedReturningTimeEnd != returningTimeEnd))

    /** True when any filter beyond the category is active in the last applied filter. */
    val hasAppliedFilters: Boolean
        get() = currentSearch.isNotBlank() || currentAmount != maximumAmount ||
            currentDateChip != null || currentHasExactRange
}
