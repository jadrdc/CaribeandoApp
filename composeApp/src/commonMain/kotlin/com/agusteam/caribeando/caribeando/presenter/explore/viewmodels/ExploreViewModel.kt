package com.agusteam.caribeando.presenter.explore.viewmodels

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.mappers.toDomain
import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.data.util.IS_CONFIRMED
import com.agusteam.caribeando.data.util.POPULAR
import com.agusteam.caribeando.data.util.REFRESH_TOKEN
import com.agusteam.caribeando.data.util.TOKEN
import com.agusteam.caribeando.domain.models.CategoryModel
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.models.TripModel
import com.agusteam.caribeando.domain.usecase.GetCategoryUseCase
import com.agusteam.caribeando.domain.usecase.GetLocalProfileUseCase
import com.agusteam.caribeando.domain.usecase.GetPaginatedTripsUseCase
import com.agusteam.caribeando.domain.usecase.MarkFavoriteTripUseCase
import com.agusteam.caribeando.domain.usecase.UnmarkedFavoriteTripUseCase
import com.agusteam.caribeando.presenter.explore.state.ExploreFilterState
import com.agusteam.caribeando.presenter.explore.state.ExploreState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModel(
    private val logger: CrashReporter,
    val getCategoryUseCase: GetCategoryUseCase,
    private val getPaginatedTripsUseCase: GetPaginatedTripsUseCase,
    val markFavoriteTripUseCase: MarkFavoriteTripUseCase,
    val unmarkedFavoriteTripUseCase: UnmarkedFavoriteTripUseCase,
    private val getLocalUserProfile: GetLocalProfileUseCase,

    ) : GenericViewModel<ExploreState, ExploreEvent>(ExploreState()) {

    private var paginationInProgress = false

    private suspend fun getPaginatedTrips(
        categoryId: String = "",
        endingAmount: Int = 0,
        search: String = "",
        leavingTimeStart: Instant = Clock.System.now(),
        returningTimeEnd: Instant = Clock.System.now(),
        showRefresh: Boolean = false
    ) {
        if (paginationInProgress) return
        paginationInProgress = true

        try {
            if (showRefresh) {
                getPaginatedTripsUseCase.resetPagination()
            }

            when (val paginationResult = getPaginatedTripsUseCase.loadMore(
                category = categoryId,
                endingAmount = endingAmount,
                search = search,
                leavingTimeStart = leavingTimeStart,
                returningTimeEnd = returningTimeEnd
            )) {
                is OperationResult.Error -> {
                    setState { copy(showUIError = true) }
                    logger.recordException(paginationResult.exception)
                }

                is OperationResult.Success -> {
                    val tripList = paginationResult.data.map { trip ->
                        trip.toDomain()
                    }
                    setState { copy(items = tripList) }
                }
            }
        } finally {
            paginationInProgress = false
        }
    }


    private suspend fun loadToken() {
        if (!Token.isValid) {
            getLocalUserProfile()
                .mapLatest { preferences ->
                    val refresh = preferences[stringPreferencesKey(REFRESH_TOKEN)] ?: ""
                    val token = preferences[stringPreferencesKey(TOKEN)] ?: ""
                    val isConfirmed = preferences[booleanPreferencesKey(IS_CONFIRMED)] ?: false
                    Token.token = token
                    Token.refreshToken = refresh
                    Token.isConfirmed = isConfirmed
                }
                .launchIn(viewModelScope)
        }
    }

    private fun getDefaultReturningDate(): Instant {
        val localDateTime =
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val futureDate = localDateTime.date.plus(3, DateTimeUnit.MONTH)
        val futureTime = localDateTime.time

        return LocalDateTime(
            futureDate,
            futureTime
        ).toInstant(TimeZone.currentSystemDefault())
    }

    private suspend fun initialLoad() {
        setState { copy(categoryState = categoryState.copy(isLoadingSkeleton = true)) }
        when (val result = getCategoryUseCase()) {
            is OperationResult.Error -> {
                setState { copy(showUIError = true) }
                logger.recordException(result.exception)
            }

            is OperationResult.Success -> {
                val categories = result.data.map { it.copy(isSelected = it.description == POPULAR) }
                val popular = categories.firstOrNull { it.isSelected }
                if (categories.isNotEmpty()) {
                    val localDateTime =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    val futureDate = localDateTime.date.plus(3, DateTimeUnit.MONTH)
                    val futureTime = localDateTime.time
                    val returningTimeEnd = LocalDateTime(
                        futureDate,
                        futureTime
                    ).toInstant(TimeZone.currentSystemDefault())
                    //TODO TEMAS DEL FAVORITO
                    getPaginatedTrips(
                        showRefresh = true,
                        categoryId = popular?.id ?: "",
                        returningTimeEnd = returningTimeEnd
                    )
                    setState {
                        copy(
                            categoryState = categoryState.copy(
                                selectedCategoryModel = categories.firstOrNull { it.isSelected },
                                categories = categories
                            ),
                            filterState = ExploreFilterState(
                                selectedCategoryModel = popular,
                            ),
                        )
                    }
                } else {
                    setState { copy(showUIError = true) }
                }
            }
        }
        setState {
            copy(
                categoryState = categoryState.copy(
                    isLoadingSkeleton = false, isLoadingCategory = false
                )
            )
        }
    }

    private suspend fun updateSelectedCategory(categoryModel: CategoryModel) {
        setState {
            copy(
                categoryState = categoryState.copy(isLoadingSkeleton = true),
            )
        }
        setState {
            copy(
                filterState = filterState.copy(selectedCategoryModel = categoryModel),
                categoryState = categoryState.copy(
                    selectedCategoryModel = categoryModel,
                    categories = updateCategoriesSelection(categoryState.categories, categoryModel)
                ),
            )
        }
        getPaginatedTrips(
            categoryId = categoryModel.id, showRefresh = true,
            returningTimeEnd = getDefaultReturningDate()
        )
        setState { copy(categoryState = categoryState.copy(isLoadingSkeleton = false)) }

    }

    private suspend fun applySelectedFilter(categoryModel: CategoryModel) {
        setState {
            copy(
                shouldBottomModal = false,
                categoryState = categoryState.copy(
                    selectedCategoryModel = categoryModel,
                    categories = updateCategoriesSelection(categoryState.categories, categoryModel)
                ),
            )

        }
    }


    private fun updateCategoriesSelection(
        categories: List<CategoryModel>, selectedModel: CategoryModel
    ): List<CategoryModel> {
        return categories.map { category ->
            category.copy(isSelected = category == selectedModel)
        }
    }


    private suspend fun toggleFilterModal(value: Boolean) {
        setState {
            if (value) {
                copy(
                    shouldBottomModal = value
                )
            } else {
                copy(
                    filterState = filterState.copy(
                        selectedCategoryModel = categoryState.selectedCategoryModel,
                        // 2026-10-09: was "" -- dismissing the sheet wiped an applied search
                        // from the sheet's field even though the feed was still filtered by it.
                        searchText = filterState.currentSearch,
                        selectedAmount = filterState.currentAmount,
                        // 2026-10-09: closing without applying now also discards pending date
                        // changes (chip / exact range), same as category/search/amount above.
                        selectedLeavingTimeStart = filterState.leavingTimeStart,
                        selectedReturningTimeEnd = filterState.returningTimeEnd,
                        selectedDateChip = filterState.currentDateChip,
                        selectedHasExactRange = filterState.currentHasExactRange,
                    ), shouldBottomModal = value
                )
            }
        }
    }

    private suspend fun clearFilter() {
        setState {
            copy(
                filterState = filterState.copy(
                    selectedLeavingTimeStart = Instant.fromEpochMilliseconds(filterState.leavingTimeStart.toEpochMilliseconds()),
                    selectedReturningTimeEnd = Instant.fromEpochMilliseconds(filterState.returningTimeEnd.toEpochMilliseconds()),
                    selectedAmount = filterState.currentAmount,
                    selectedCategoryModel = categoryState.selectedCategoryModel,
                    searchText = filterState.currentSearch,
                    selectedDateChip = filterState.currentDateChip,
                    selectedHasExactRange = filterState.currentHasExactRange,
                )
            )
        }
    }

    private suspend fun updateFilterCategory(categoryModel: CategoryModel) {
        setState {
            copy(
                filterState = filterState.copy(selectedCategoryModel = categoryModel)
            )
        }
    }

    private suspend fun updateSearchText(value: String) {
        setState {
            copy(
                filterState = filterState.copy(searchText = value)
            )
        }
    }

    private suspend fun updateSelectedAmount(value: Float) {
        setState {
            copy(
                filterState = filterState.copy(selectedAmount = value)
            )
        }
    }


    private suspend fun updateShoppingitem(
        item: TripModel
    ) {
        setState { copy(isLoading = true) }
        val markState = !item.isSavedForLater
        val result = if (markState) {
            markFavoriteTripUseCase(tripId = item.id)
        } else {
            unmarkedFavoriteTripUseCase(tripId = item.id)
        }
        when (result) {
            is OperationResult.Error -> {
                logger.recordException(result.exception)
                onErrorHappened(
                    true,
                    "Error cambiando el estado de viaje",
                    "No se pudo completar la operacion,intente mas tarde."
                )
            }

            is OperationResult.Success -> {
                val newList = state.value.items.map {
                    if (it.id == item.id) {
                        it.copy(isSavedForLater = markState)
                    } else {
                        it
                    }
                }
                setState {
                    copy(items = newList)
                }
            }
        }
        setState { copy(isLoading = false) }
    }

    /**
     * 2026-10-09 (design handoff, screen 7): a single-day chip. `null` = "Cualquier fecha"
     * (back to the default now..+3 months window). Mutually exclusive with an exact range.
     */
    private suspend fun selectDateChip(date: LocalDate?) {
        val tz = TimeZone.currentSystemDefault()
        setState {
            if (date == null) {
                copy(
                    filterState = filterState.copy(
                        selectedDateChip = null,
                        selectedHasExactRange = false,
                        selectedLeavingTimeStart = Clock.System.now(),
                        selectedReturningTimeEnd = getDefaultReturningDate(),
                    )
                )
            } else {
                copy(
                    filterState = filterState.copy(
                        selectedDateChip = date,
                        selectedHasExactRange = false,
                        selectedLeavingTimeStart = date.atStartOfDayIn(tz),
                        selectedReturningTimeEnd = LocalDateTime(date, LocalTime(23, 59, 59)).toInstant(tz),
                    )
                )
            }
        }
    }

    /**
     * 2026-10-09 (design handoff, screens 7 and 9): "Borrar filtros" resets all four filters
     * (category, budget, dates, text) to the initial-load defaults, closes the sheet and reloads
     * -- previously the sheet's button only reverted pending edits and the empty state's button
     * only reset the category.
     */
    private suspend fun resetAllFilters() {
        val categories = state.value.categoryState.categories
        val defaultCategory = categories.firstOrNull { it.description == POPULAR }
            ?: CategoryModel(id = "", description = "Todo", isSelected = true)
        setState {
            copy(
                shouldBottomModal = false,
                filterState = ExploreFilterState(selectedCategoryModel = defaultCategory),
                categoryState = categoryState.copy(
                    selectedCategoryModel = defaultCategory,
                    categories = updateCategoriesSelection(categories, defaultCategory),
                    isLoadingSkeleton = true,
                ),
            )
        }
        getPaginatedTrips(
            categoryId = defaultCategory.id,
            showRefresh = true,
            returningTimeEnd = getDefaultReturningDate()
        )
        setState { copy(categoryState = categoryState.copy(isLoadingSkeleton = false)) }
    }

    private suspend fun onErrorHappened(value: Boolean, title: String = "", message: String = "") {
        val errorModel = if (!value) {
            null
        } else {
            ErrorModel(title = title, message = message)
        }
        setState {
            copy(
                errorModel = errorModel
            )
        }
    }

    fun onExploreEventChanged(event: ExploreEvent) {
        viewModelScope.launch {
            when (event) {
                is ExploreEvent.InitLoad -> {
                    loadToken()
                    initialLoad()
                }

                is ExploreEvent.RefreshContent -> {
                    setState {
                        copy(
                            isRefreshing = true,
                            categoryState = categoryState.copy(isLoadingSkeleton = true)
                        )
                    }
                    val category =
                        state.value.categoryState.categories.firstOrNull { it.description == POPULAR }
                    if (category != null) {
                        getPaginatedTrips(
                            category.id,
                            showRefresh = true,
                            returningTimeEnd = getDefaultReturningDate()
                        )
                    }

                    setState {
                        copy(
                            isRefreshing = false,
                            categoryState = categoryState.copy(isLoadingSkeleton = false)
                        )
                    }
                }

                is ExploreEvent.OnCategorySelected -> {
                    updateSelectedCategory(event.categoryModel)
                }

                is ExploreEvent.OnFilterChanged -> {
                    toggleFilterModal(event.value)
                }

                is ExploreEvent.OnFilterCleared -> {
                    clearFilter()
                }

                is ExploreEvent.OnFilterApplied -> {
                    state.value.filterState.selectedCategoryModel?.let {
                        applySelectedFilter(it)
                        setState {
                            copy(
                                categoryState = categoryState.copy(isLoadingSkeleton = true),
                                filterState = filterState.copy(
                                    leavingTimeStart = filterState.selectedLeavingTimeStart,
                                    returningTimeEnd = filterState.selectedReturningTimeEnd,
                                    currentAmount = filterState.selectedAmount,
                                    currentSearch = filterState.searchText,
                                    currentDateChip = filterState.selectedDateChip,
                                    currentHasExactRange = filterState.selectedHasExactRange,
                                )
                            )
                        }
                        getPaginatedTrips(
                            returningTimeEnd = state.value.filterState.selectedReturningTimeEnd,
                            leavingTimeStart = state.value.filterState.selectedLeavingTimeStart,
                            categoryId = it.id,
                            endingAmount = state.value.filterState.selectedAmount.toInt(),
                            search = state.value.filterState.searchText, showRefresh = true
                        )
                        setState { copy(categoryState = categoryState.copy(isLoadingSkeleton = false)) }
                    }
                }

                is ExploreEvent.OnFilterCategorySelected -> {
                    updateFilterCategory(event.categoryModel)
                }

                is ExploreEvent.OnFilterSearchChanged -> {
                    updateSearchText(event.search)
                }

                is ExploreEvent.OnSelectedFilterAmount -> {
                    updateSelectedAmount(event.selectedAmount)
                }

                is ExploreEvent.OnShoppingItemMarked -> {
                    updateShoppingitem(event.item)
                }

                is ExploreEvent.OnErrorModalAccepted -> {
                    onErrorHappened(false)
                }

                is ExploreEvent.LoadMoreTrips -> {
                    // 2026-10-09: next pages now use the APPLIED filter (current* values and the
                    // applied date window). Previously they used the sheet's pending values and
                    // always the default date window, so scrolling a filtered feed could append
                    // trips that didn't match it.
                    val filter = state.value.filterState
                    getPaginatedTrips(
                        leavingTimeStart = filter.leavingTimeStart,
                        returningTimeEnd = filter.returningTimeEnd,
                        categoryId = state.value.categoryState.selectedCategoryModel?.id ?: "",
                        endingAmount = filter.currentAmount.toInt(),
                        search = filter.currentSearch,
                        showRefresh = false
                    )
                }

                is ExploreEvent.OnFilterSelectedDateRange -> setState {
                    copy(
                        filterState = filterState.copy(
                            selectedReturningTimeEnd = event.returningDate,
                            selectedLeavingTimeStart = event.leavingDate,
                            // 2026-10-09: an exact range (Calendario) replaces any date chip.
                            selectedHasExactRange = true,
                            selectedDateChip = null,
                        )
                    )
                }

                is ExploreEvent.OnFilterDateChipSelected -> selectDateChip(event.date)

                is ExploreEvent.OnFilterResetAll -> resetAllFilters()
            }
        }
    }
}

sealed interface ExploreEvent {
    data object InitLoad : ExploreEvent
    data object OnErrorModalAccepted : ExploreEvent
    class OnCategorySelected(val categoryModel: CategoryModel) : ExploreEvent
    class OnShoppingItemMarked(val item: TripModel) : ExploreEvent
    class OnFilterChanged(val value: Boolean) : ExploreEvent
    data object OnFilterCleared : ExploreEvent
    data object OnFilterApplied : ExploreEvent
    data class OnSelectedFilterAmount(val selectedAmount: Float) : ExploreEvent
    data class OnFilterCategorySelected(val categoryModel: CategoryModel) : ExploreEvent
    data class OnFilterSearchChanged(val search: String) : ExploreEvent
    data class OnFilterSelectedDateRange(val leavingDate: Instant, val returningDate: Instant) :
        ExploreEvent

    // 2026-10-09 (design handoff, screens 7 and 9)
    data class OnFilterDateChipSelected(val date: LocalDate?) : ExploreEvent
    data object OnFilterResetAll : ExploreEvent

    data object RefreshContent : ExploreEvent
    data object LoadMoreTrips : ExploreEvent
}
