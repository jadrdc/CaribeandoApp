package com.agusteam.caribeando.presenter.explore.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.Chip
import com.agusteam.caribeando.presenter.common.SearchField
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.explore.screen.chipLabel
import com.agusteam.caribeando.presenter.explore.screen.shortLabel
import com.agusteam.caribeando.presenter.explore.state.ExploreState
import com.agusteam.caribeando.presenter.explore.viewmodels.ExploreEvent
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.any_amount
import caribeando.composeapp.generated.resources.any_date
import caribeando.composeapp.generated.resources.budget_up_to
import caribeando.composeapp.generated.resources.cancel
import caribeando.composeapp.generated.resources.clear
import caribeando.composeapp.generated.resources.exact_dates
import caribeando.composeapp.generated.resources.filter
import caribeando.composeapp.generated.resources.max_amount
import caribeando.composeapp.generated.resources.see_results_one
import caribeando.composeapp.generated.resources.see_results_other
import caribeando.composeapp.generated.resources.trip_categories
import caribeando.composeapp.generated.resources.when_to_travel
import kotlin.time.Clock

/**
 * The handoff's "7. Filtrar" bottom sheet.
 *
 * 2026-08-20 (design handoff pass): the `AmoutSliderPicker` slider was replaced with 4 discrete
 * budget `Chip`s ("Cualquiera" maps to `maximumAmount`, the slider's old ceiling).
 *
 * 2026-10-09 (design handoff pass, part 3):
 * - The inline `ModernDateRangePicker` is gone. "¿Cuándo quisieras viajar?" now shows chips --
 *   "Cualquier fecha" plus the next four weekend days -- and a "Fechas exactas" link that opens
 *   the new full-screen "8. Calendario" ([onOpenCalendar]). When an exact range is set it shows
 *   as a leading selected chip (tapping it reopens the calendar). A day chip and a range are
 *   mutually exclusive (enforced in `ExploreViewModel`).
 * - "Borrar filtros" now resets ALL four filters, closes the sheet and reloads
 *   (`OnFilterResetAll`), per spec -- it used to only revert unapplied edits.
 * - The primary button: there is still no API to preview a result count without applying (trip
 *   loading is a stateful paginator), so the "Ver N resultados" count is only shown while the
 *   sheet's choices match what the feed is already showing -- i.e. when N is true. With pending
 *   changes, or when nothing is filtered at all, it reads "Filtrar" (the spec's own fallback
 *   label) instead of a stale number.
 * - Gutter 16 -> 20dp, and the remaining hardcoded copy moved to strings.xml.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeFilterContent(
    exploreState: ExploreState,
    onEventChanged: (ExploreEvent) -> Unit,
    onOpenCalendar: () -> Unit = {},
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    val filter = exploreState.filterState
    val anyAmountLabel = stringResource(Res.string.any_amount)
    val budgetOptions = listOf(
        anyAmountLabel to filter.maximumAmount,
        stringResource(Res.string.budget_up_to, "2,000") to 2000f,
        stringResource(Res.string.budget_up_to, "4,000") to 4000f,
        stringResource(Res.string.budget_up_to, "6,000") to 6000f,
    )
    val upcomingWeekendDays = remember { nextWeekendDays(count = 4) }

    val hasPending = filter.hasPendingChanges(exploreState.categoryState.selectedCategoryModel?.id)
    val resultCount = exploreState.items.size
    val primaryLabel = when {
        hasPending || !filter.hasAppliedFilters -> stringResource(Res.string.filter)
        resultCount == 1 -> stringResource(Res.string.see_results_one)
        else -> stringResource(Res.string.see_results_other, resultCount)
    }

    LazyColumn(
        Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.filter),
                    style = textStyles.screenTitleCompact,
                    color = colors.textPrimary
                )
                Icon(
                    painter = painterResource(Res.drawable.cancel),
                    contentDescription = null,
                    tint = colors.iconInactive,
                    modifier = Modifier
                        .size(22.dp)
                        .bounceClick(scaleDown = 0.9f) {
                            onEventChanged(ExploreEvent.OnFilterChanged(false))
                        }
                )
            }
        }
        item {
            Box(modifier = Modifier.padding(top = 18.dp)) {
                SearchField(
                    query = filter.searchText,
                    onQueryChange = {
                        onEventChanged(ExploreEvent.OnFilterSearchChanged(it))
                    },
                )
            }
        }
        item {
            Text(
                modifier = Modifier.padding(top = 24.dp),
                color = colors.textPrimary,
                text = stringResource(Res.string.trip_categories),
                style = textStyles.sectionHeader
            )
            FlowRow(
                Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                exploreState.categoryState.categories.forEach { category ->
                    Chip(
                        title = category.description,
                        isSelected = category.id == (filter.selectedCategoryModel?.id ?: ""),
                        iconUrl = category.image,
                        onClick = {
                            onEventChanged(ExploreEvent.OnFilterCategorySelected(category))
                        }
                    )
                }
            }
        }
        item {
            Text(
                modifier = Modifier.padding(top = 24.dp),
                color = colors.textPrimary,
                text = stringResource(Res.string.max_amount),
                style = textStyles.sectionHeader
            )
            FlowRow(
                Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                budgetOptions.forEach { (label, amount) ->
                    Chip(
                        title = label,
                        isSelected = filter.selectedAmount == amount,
                        onClick = {
                            onEventChanged(ExploreEvent.OnSelectedFilterAmount(amount))
                        }
                    )
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = colors.textPrimary,
                    text = stringResource(Res.string.when_to_travel),
                    style = textStyles.sectionHeader
                )
                Text(
                    text = stringResource(Res.string.exact_dates),
                    color = colors.redText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.bounceClick(scaleDown = 0.95f, indication = null) { onOpenCalendar() }
                )
            }
            FlowRow(
                Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (filter.selectedHasExactRange) {
                    val tz = TimeZone.currentSystemDefault()
                    val s = filter.selectedLeavingTimeStart.toLocalDateTime(tz).date
                    val e = filter.selectedReturningTimeEnd.toLocalDateTime(tz).date
                    Chip(
                        title = if (s == e) s.shortLabel() else "${s.shortLabel()} – ${e.shortLabel()}",
                        isSelected = true,
                        onClick = onOpenCalendar
                    )
                }
                Chip(
                    title = stringResource(Res.string.any_date),
                    isSelected = !filter.selectedHasExactRange && filter.selectedDateChip == null,
                    onClick = { onEventChanged(ExploreEvent.OnFilterDateChipSelected(null)) }
                )
                upcomingWeekendDays.forEach { day ->
                    Chip(
                        title = day.chipLabel(),
                        isSelected = filter.selectedDateChip == day,
                        onClick = { onEventChanged(ExploreEvent.OnFilterDateChipSelected(day)) }
                    )
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextLink(
                    text = stringResource(Res.string.clear),
                    onClick = { onEventChanged(ExploreEvent.OnFilterResetAll) },
                    color = colors.textPrimary,
                    underline = true,
                )

                ActionButton(
                    text = primaryLabel,
                    modifier = Modifier.fillMaxWidth(0.62f)
                ) {
                    if (hasPending) {
                        onEventChanged(ExploreEvent.OnFilterApplied)
                    } else {
                        // Nothing changed: the feed already shows these results.
                        onEventChanged(ExploreEvent.OnFilterChanged(false))
                    }
                }
            }
        }
    }
}

/** The next [count] Saturdays/Sundays starting today (today included if it's a weekend day). */
private fun nextWeekendDays(count: Int): List<LocalDate> {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return generateSequence(today) { it.plus(DatePeriod(days = 1)) }
        .filter { it.dayOfWeek == DayOfWeek.SATURDAY || it.dayOfWeek == DayOfWeek.SUNDAY }
        .take(count)
        .toList()
}
