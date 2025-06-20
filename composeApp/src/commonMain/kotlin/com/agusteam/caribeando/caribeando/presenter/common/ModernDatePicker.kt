package com.agusteam.caribeando.presenter.common

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

fun Month.nameInSpanish(): String = when (this) {
    Month.JANUARY -> "Enero"
    Month.FEBRUARY -> "Febrero"
    Month.MARCH -> "Marzo"
    Month.APRIL -> "Abril"
    Month.MAY -> "Mayo"
    Month.JUNE -> "Junio"
    Month.JULY -> "Julio"
    Month.AUGUST -> "Agosto"
    Month.SEPTEMBER -> "Septiembre"
    Month.OCTOBER -> "Octubre"
    Month.NOVEMBER -> "Noviembre"
    Month.DECEMBER -> "Diciembre"
    else -> ""
}


fun isLeapYear(year: Int): Boolean = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

val MonthDayCount = listOf(
    listOf(31, 31), // Jan
    listOf(28, 29), // Feb
    listOf(31, 31), // Mar
    listOf(30, 30), // Apr
    listOf(31, 31), // May
    listOf(30, 30), // Jun
    listOf(31, 31), // Jul
    listOf(31, 31), // Aug
    listOf(30, 30), // Sep
    listOf(31, 31), // Oct
    listOf(30, 30), // Nov
    listOf(31, 31)  // Dec
)

fun LocalDate.formatShort(): String =
    "${dayOfMonth.toString().padStart(2, '0')}/${monthNumber.toString().padStart(2, '0')}/$year"

fun YearMonth.formatLong(): String =
    "${month.name.lowercase().replaceFirstChar { it.uppercase() }} $year"

fun Int.isLeapYear(year: Int): Boolean = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

// Format functions using Spanish months
/*
fun LocalDate.formatShort(): String {
    val month = this.month.spanishName.take(3)
    return "$month ${this.dayOfMonth.toString().padStart(2, '0')}, ${this.year}"
}

fun YearMonth.formatLong(): String {
    val month = this.month.spanishName
    return "$month ${this.year}"
}*/
data class YearMonth(val year: Int, val month: Month) {
    fun plus(months: Int): YearMonth {
        val totalMonths = (year * 12 + (month.ordinal)) + months
        val newYear = totalMonths / 12
        val newMonth = Month.values()[((totalMonths % 12) + 12) % 12]
        return YearMonth(newYear, newMonth)
    }

    fun minus(months: Int): YearMonth = plus(-months)
    fun lengthOfMonth(): Int = month.daysIn(year)
}

// Extension to get days in month for a given year
val Month.daysIn: (Int) -> Int
    get() = { year ->
        when (this) {
            Month.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
    }


@Composable
fun ModernDatePicker(
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val defaultDate = now.minus(DatePeriod(years = 18))
    val maxAllowedDate = defaultDate
    val maxSelectableYear = defaultDate.year

    var selectedDate by remember { mutableStateOf(defaultDate) }
    var selectedMonth by remember { mutableStateOf(YearMonth(defaultDate.year, defaultDate.month)) }
    var expanded by remember { mutableStateOf(false) }
    var showMonthDialog by remember { mutableStateOf(false) }
    var showYearDialog by remember { mutableStateOf(false) }

    val transition = updateTransition(expanded, label = "calendar_transition")
    val cardElevation by transition.animateDp(label = "elevation") { if (it) 8.dp else 2.dp }
    val roundedCorners by transition.animateDp(label = "corner_radius") { if (it) 8.dp else 24.dp }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        shape = RoundedCornerShape(roundedCorners),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                        text = selectedDate.formatShort(),
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedMonth.month.nameInSpanish(),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { showMonthDialog = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "${selectedMonth.year}",
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { showYearDialog = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("D", "L", "M", "M", "J", "V", "S").forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val firstDayOfMonth = LocalDate(selectedMonth.year, selectedMonth.month, 1)
                    val daysInMonth = selectedMonth.lengthOfMonth()
                    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.isoDayNumber % 7)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.height(240.dp)
                    ) {
                        items(firstDayOfWeek) {
                            Box(modifier = Modifier.aspectRatio(1f))
                        }

                        items(daysInMonth) { day ->
                            val date = LocalDate(selectedMonth.year, selectedMonth.month, day + 1)
                            val isSelected = date == selectedDate
                            val isEnabled = date <= maxAllowedDate

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            else -> Color.Transparent
                                        }
                                    )
                                    .let {
                                        if (isEnabled) it.clickable {
                                            selectedDate = date
                                            onDateSelected(date)
                                        } else it
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${day + 1}",
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        isEnabled -> MaterialTheme.colorScheme.onSurface
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.3f
                                        )
                                    },
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

    if (showMonthDialog) {
        AlertDialog(
            onDismissRequest = { showMonthDialog = false },
            title = { Text("Selecciona el mes") },
            text = {
                Column {
                    Month.values().forEach { month ->
                        val newMonth = YearMonth(selectedMonth.year, month)
                        if (newMonth.year < maxAllowedDate.year || (newMonth.year == maxAllowedDate.year && month <= maxAllowedDate.month)) {
                            Text(
                                text = month.nameInSpanish(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedMonth = newMonth
                                        showMonthDialog = false
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    if (showYearDialog) {
        val years = (1950..maxSelectableYear).toList()

        AlertDialog(
            onDismissRequest = { showYearDialog = false },
            title = { Text("Selecciona el a\u00f1o") },
            text = {
                Column(modifier = Modifier.height(200.dp)) {
                    LazyColumn {
                        items(years.size) { index ->
                            val year = years[index]
                            Text(
                                text = "$year",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (year == maxAllowedDate.year && selectedMonth.month > maxAllowedDate.month) {
                                            selectedMonth = YearMonth(year, maxAllowedDate.month)
                                        } else {
                                            selectedMonth = YearMonth(year, selectedMonth.month)
                                        }
                                        showYearDialog = false
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}
