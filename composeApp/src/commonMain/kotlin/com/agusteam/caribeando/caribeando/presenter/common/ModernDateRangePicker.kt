package com.agusteam.caribeando.presenter.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

@Composable
fun ModernDateRangePicker(
    startDate: Instant,
    endDate: Instant,
    onRangeSelected: (Instant, Instant) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeZone = TimeZone.currentSystemDefault()

    val externalStartDate = startDate.toLocalDateTime(timeZone).date
    val externalEndDate = endDate.toLocalDateTime(timeZone).date

    var selectedStart by remember { mutableStateOf<LocalDate?>(externalStartDate) }
    var selectedEnd by remember { mutableStateOf<LocalDate?>(externalEndDate) }

    // Sync with external values when they change
    LaunchedEffect(startDate, endDate) {
        selectedStart = externalStartDate
        selectedEnd = externalEndDate
    }

    var selectedMonth by remember {
        mutableStateOf(
            YearMonth(
                Clock.System.todayIn(timeZone).year,
                Clock.System.todayIn(timeZone).month
            )
        )
    }

    var expanded by remember { mutableStateOf(false) }

    val transition = updateTransition(expanded, label = "calendar_transition")
    val roundedCorners by transition.animateDp(label = "corner_radius") { isExpanded ->
        if (isExpanded) 8.dp else 24.dp
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(roundedCorners),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
                        text = "${selectedStart?.formatShort() ?: "Inicio"} - ${selectedEnd?.formatShort() ?: "Fin"}",
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
                    // Month navigation
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
                                contentDescription = "Mes anterior"
                            )
                        }
                        Text(
                            text = selectedMonth.formatLong(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(onClick = { selectedMonth = selectedMonth.plus(1) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "Mes siguiente"
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

                    // Calendar
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

                        items(daysInMonth) { index ->
                            val day = index + 1
                            val date = LocalDate(selectedMonth.year, selectedMonth.month, day)
                            val isSelected = date == selectedStart || date == selectedEnd
                            val isInRange = selectedStart != null && selectedEnd != null &&
                                    date > selectedStart!! && date < selectedEnd!!

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isInRange -> MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.2f
                                            )

                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable {
                                        when {
                                            selectedStart == null || (selectedStart != null && selectedEnd != null) -> {
                                                selectedStart = date
                                                selectedEnd = null
                                            }

                                            date < selectedStart!! -> {
                                                selectedEnd = selectedStart
                                                selectedStart = date
                                            }

                                            date == selectedStart -> {
                                                selectedStart = date
                                                selectedEnd = null
                                            }

                                            else -> {
                                                selectedEnd = date
                                            }
                                        }

                                        if (selectedStart != null) {
                                            onRangeSelected(
                                                selectedStart!!.atStartOfDayIn(timeZone),
                                                (selectedEnd ?: selectedStart)!!.atStartOfDayIn(
                                                    timeZone
                                                )
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
