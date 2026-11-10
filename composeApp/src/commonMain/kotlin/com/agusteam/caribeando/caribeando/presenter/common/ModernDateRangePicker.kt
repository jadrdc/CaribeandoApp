package com.agusteam.caribeando.presenter.common

import caribeando.composeapp.generated.resources.previous_month
import caribeando.composeapp.generated.resources.next_month
import caribeando.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.range_start
import caribeando.composeapp.generated.resources.range_end
import caribeando.composeapp.generated.resources.select_year_description
import caribeando.composeapp.generated.resources.select_year
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import kotlin.time.Clock

@Composable
fun ModernDateRangePicker(
    startDate: Instant,
    endDate: Instant,
    onRangeSelected: (Instant, Instant) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.todayIn(timeZone)

    val externalStartDate = startDate.toLocalDateTime(timeZone).date
    val externalEndDate = endDate.toLocalDateTime(timeZone).date

    var selectedStart by remember { mutableStateOf<LocalDate?>(externalStartDate) }
    var selectedEnd by remember { mutableStateOf<LocalDate?>(externalEndDate) }

    LaunchedEffect(startDate, endDate) {
        selectedStart = externalStartDate
        selectedEnd = externalEndDate
    }

    var selectedMonth by remember {
        mutableStateOf(YearMonth(today.year, today.month))
    }

    var expanded by remember { mutableStateOf(false) }
    var yearSelectorExpanded by remember { mutableStateOf(false) }
    val yearRange = (2000..today.year + 2).toList()

    val transition = updateTransition(expanded, label = "calendar_transition")
    val roundedCorners by transition.animateDp(label = "corner_radius") { isExpanded ->
        if (isExpanded) 8.dp else 24.dp
    }

    if (yearSelectorExpanded) {
        YearPickerDialog(
            currentYear = selectedMonth.year,
            yearRange = yearRange,
            onDismiss = { yearSelectorExpanded = false },
            onYearSelected = { year ->
                selectedMonth = YearMonth(year, selectedMonth.month)
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(roundedCorners),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${selectedStart?.formatShort() ?: stringResource(Res.string.range_start)} - ${selectedEnd?.formatShort() ?: stringResource(Res.string.range_end)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {

                    // Month + Year navigation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { selectedMonth = selectedMonth.minus(1) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(Res.string.previous_month)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { yearSelectorExpanded = true }
                        ) {
                            Text(
                                text = "${selectedMonth.month.displayName()} ${selectedMonth.year}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = stringResource(Res.string.select_year_description),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(onClick = { selectedMonth = selectedMonth.plus(1) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = stringResource(Res.string.next_month)
                            )
                        }
                    }

                    // Week headers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("D", "L", "M", "M", "J", "V", "S").forEach {
                            Text(
                                text = it,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val firstDayOfMonth = LocalDate(selectedMonth.year, selectedMonth.month, 1)
                    val daysInMonth = MonthDayCount[selectedMonth.month.ordinal][if (isLeapYear(selectedMonth.year)) 1 else 0]
                    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.isoDayNumber % 7)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.height(240.dp)
                    ) {
                        items(firstDayOfWeek) {
                            Box(modifier = Modifier.aspectRatio(1f))
                        }

                        items(daysInMonth) { index ->
                            val day = index + 1
                            val date = LocalDate(selectedMonth.year, selectedMonth.month, day)
                            val rangeStart = selectedStart
                            val rangeEnd = selectedEnd
                            val isSelected = date == rangeStart || date == rangeEnd
                            val isInRange = rangeStart != null && rangeEnd != null &&
                                    date > rangeStart && date < rangeEnd
                            val isToday = date == today

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (isToday) 1.dp else 0.dp,
                                        color = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isInRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable {
                                        val start = selectedStart
                                        val end = selectedEnd
                                        when {
                                            start == null || end != null -> {
                                                selectedStart = date
                                                selectedEnd = null
                                            }

                                            date < start -> {
                                                selectedEnd = start
                                                selectedStart = date
                                            }

                                            date == start -> {
                                                selectedStart = date
                                                selectedEnd = null
                                            }

                                            else -> {
                                                selectedEnd = date
                                            }
                                        }

                                        val newStart = selectedStart
                                        if (newStart != null) {
                                            onRangeSelected(
                                                newStart.atStartOfDayIn(timeZone),
                                                (selectedEnd ?: newStart).atStartOfDayIn(timeZone)
                                            )
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$day",
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearPickerDialog(
    currentYear: Int,
    yearRange: List<Int>,
    onDismiss: () -> Unit,
    onYearSelected: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.select_year)) },
        text = {
            Column(modifier = Modifier.height(250.dp)) {
                yearRange.forEach { year ->
                    Text(
                        text = "$year",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onYearSelected(year)
                                onDismiss()
                            },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
