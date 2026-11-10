package com.agusteam.caribeando.presenter.explore.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.calendar_apply
import caribeando.composeapp.generated.resources.calendar_clear
import caribeando.composeapp.generated.resources.calendar_hint_end
import caribeando.composeapp.generated.resources.calendar_hint_restart
import caribeando.composeapp.generated.resources.calendar_hint_start
import caribeando.composeapp.generated.resources.calendar_pick_dates
import caribeando.composeapp.generated.resources.when_to_travel
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.BackIcon
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.common.YearMonth
import com.agusteam.caribeando.presenter.common.bounceClick
import com.agusteam.caribeando.presenter.common.nameInSpanish
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import com.agusteam.caribeando.presenter.theme.caribeandoTextStyles
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.todayIn
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * The handoff's "8. Calendario" [NEW screen, existing logic] -- replaces the inline
 * `ModernDateRangePicker` that used to live inside the filter sheet.
 *
 * 2026-10-09 (design handoff pass): fixed header (back + "¿Cuándo quisieras viajar?"), the range
 * label ("22 ago – 23 ago" / "Elige tus fechas") with a hint that tracks the selection state,
 * a Sunday-first weekday row, then a scrolling list of month blocks (current month + the next 11,
 * matching the feed's forward-looking window). Footer: "Borrar" + "Aplicar fechas".
 *
 * Selection follows `ModernDateRangePicker`'s existing rules: the first tap sets the start and
 * clears the end; a tap before the start swaps them; tapping the start clears the end; otherwise
 * it sets the end. Once both ends are set, the next tap starts a new range.
 *
 * Shown as a full-screen layer over Explorar (see `ExploreScreen`) rather than a NavHost route, so
 * the pending filter-sheet state it feeds into lives in the same `ExploreViewModel` untouched.
 */
@Composable
fun CalendarScreen(
    initialStart: LocalDate?,
    initialEnd: LocalDate?,
    onBack: () -> Unit,
    onApply: (Instant, Instant) -> Unit,
) {
    val colors = caribeandoColors
    val textStyles = caribeandoTextStyles()
    val timeZone = TimeZone.currentSystemDefault()
    val today = remember { Clock.System.todayIn(timeZone) }

    var start by remember { mutableStateOf(initialStart) }
    var end by remember { mutableStateOf(initialEnd) }

    val months = remember(today) {
        val first = YearMonth(today.year, today.month)
        (0 until 12).map { first.plus(it) }
    }

    fun onDayTapped(day: LocalDate) {
        val s = start
        val e = end
        when {
            s == null || e != null -> {
                start = day
                end = null
            }
            day < s -> {
                end = s
                start = day
            }
            day == s -> end = null
            else -> end = day
        }
    }

    val hint = when {
        start == null -> stringResource(Res.string.calendar_hint_start)
        end == null -> stringResource(Res.string.calendar_hint_end)
        else -> stringResource(Res.string.calendar_hint_restart)
    }
    val rangeLabel = start?.let { s ->
        val e = end
        if (e == null || e == s) s.shortSpanish() else "${s.shortSpanish()} – ${e.shortSpanish()}"
    } ?: stringResource(Res.string.calendar_pick_dates)

    Column(Modifier.fillMaxSize().background(colors.pageBackground)) {
        Column(Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackIcon(Modifier) { onBack() }
                Text(
                    text = stringResource(Res.string.when_to_travel),
                    style = textStyles.navBarTitle,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = rangeLabel,
                color = colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(3.dp))
            Text(text = hint, color = colors.textSecondary, style = textStyles.metadata)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth()) {
                listOf("D", "L", "M", "M", "J", "V", "S").forEach {
                    Text(
                        text = it,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = colors.textSecondary,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item { Spacer(Modifier.height(4.dp)) }
            items(months) { month ->
                MonthBlock(
                    month = month,
                    today = today,
                    start = start,
                    end = end,
                    onDayTapped = ::onDayTapped,
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }

        HorizontalDivider(thickness = 1.dp, color = colors.hairline)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextLink(
                text = stringResource(Res.string.calendar_clear),
                onClick = {
                    start = null
                    end = null
                },
                color = colors.textPrimary,
                underline = true,
            )
            ActionButton(
                isValid = start != null,
                text = stringResource(Res.string.calendar_apply),
                modifier = Modifier.fillMaxWidth(0.62f),
            ) {
                val s = start ?: return@ActionButton
                val e = end ?: s
                onApply(
                    s.atStartOfDayIn(timeZone),
                    LocalDateTime(e, LocalTime(23, 59, 59)).toInstant(timeZone)
                )
            }
        }
    }
}

@Composable
private fun MonthBlock(
    month: YearMonth,
    today: LocalDate,
    start: LocalDate?,
    end: LocalDate?,
    onDayTapped: (LocalDate) -> Unit,
) {
    val colors = caribeandoColors
    val first = LocalDate(month.year, month.month, 1)
    // Week starts Sunday, matching the existing picker: Sunday (iso 7) -> column 0.
    val leading = first.dayOfWeek.isoDayNumber % 7
    val days = month.lengthOfMonth()
    val cells: List<LocalDate?> = List(leading) { null } + (1..days).map { LocalDate(month.year, month.month, it) }
    val rangeFill = colors.navyFill.copy(alpha = 0.12f)

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = "${month.month.nameInSpanish()} ${month.year}",
            color = colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                for (i in 0 until 7) {
                    val day = week.getOrNull(i)
                    Box(Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                        if (day != null) {
                            val isPast = day < today
                            val isEndpoint = day == start || day == end
                            val inRange = start != null && end != null && day > start && day < end
                            val bg = when {
                                isEndpoint -> Modifier.background(colors.navyFill, CircleShape)
                                inRange -> Modifier.background(rangeFill, CircleShape)
                                else -> Modifier
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .then(bg)
                                    .then(
                                        if (isPast) Modifier
                                        else Modifier.bounceClick(scaleDown = 0.9f, indication = null) { onDayTapped(day) }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.dayOfMonth.toString(),
                                    fontSize = 13.sp,
                                    fontWeight = when {
                                        isEndpoint -> FontWeight.SemiBold
                                        inRange -> FontWeight.Medium
                                        else -> FontWeight.Normal
                                    },
                                    color = when {
                                        isPast -> colors.disabledText
                                        isEndpoint -> androidx.compose.ui.graphics.Color.White
                                        else -> colors.textPrimary
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val SPANISH_SHORT_MONTHS = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")

/** "22 ago" -- the handoff's range-label format. */
internal fun LocalDate.shortSpanish(): String = "$dayOfMonth ${SPANISH_SHORT_MONTHS[month.ordinal]}"

/** "Sáb 11 oct" -- used by the filter sheet's concrete date chips. */
internal fun LocalDate.chipSpanish(): String {
    val dow = when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lun"
        DayOfWeek.TUESDAY -> "Mar"
        DayOfWeek.WEDNESDAY -> "Mié"
        DayOfWeek.THURSDAY -> "Jue"
        DayOfWeek.FRIDAY -> "Vie"
        DayOfWeek.SATURDAY -> "Sáb"
        else -> "Dom"
    }
    return "$dow ${shortSpanish()}"
}
