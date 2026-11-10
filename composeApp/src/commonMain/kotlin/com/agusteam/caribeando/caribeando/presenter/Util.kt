package com.agusteam.caribeando.presenter

import caribeando.composeapp.generated.resources.Res
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.getStringArray
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.months_short
import caribeando.composeapp.generated.resources.time_today
import caribeando.composeapp.generated.resources.time_past_date
import caribeando.composeapp.generated.resources.time_months_one
import caribeando.composeapp.generated.resources.time_months_other
import caribeando.composeapp.generated.resources.time_days_one
import caribeando.composeapp.generated.resources.time_days_other
import caribeando.composeapp.generated.resources.time_period_months
import caribeando.composeapp.generated.resources.time_period_month
import caribeando.composeapp.generated.resources.time_period_year
import caribeando.composeapp.generated.resources.time_period_years
import com.agusteam.caribeando.data.network.ApiConfig
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun formatMoney(amount: String): String {
    return try {
        val parts = amount.split(".")
        val integerPart = parts[0].reversed().chunked(3).joinToString(",").reversed()
        val decimalPart = if (parts.size > 1) parts[1].padEnd(2, '0').take(2) else "00"
        "$integerPart.$decimalPart"
    } catch (e: Exception) {
        "Invalid amount"
    }
}

fun Double.toIntOrNullSafe(): Int {
    return if (this.isFinite() && this >= Int.MIN_VALUE && this <= Int.MAX_VALUE) {
        this.toInt()
    } else {
        0
    }
}

suspend fun formatInstant(
    instant: Instant,
    zoneId: TimeZone = TimeZone.currentSystemDefault()
): String {
    // Convert Instant to LocalDateTime in the provided TimeZone
    val dateTime = instant.toLocalDateTime(zoneId)

    val monthsShort = getStringArray(Res.array.months_short)

    // Extract day, month, year, hour, and minute
    val day = dateTime.date.dayOfMonth
    val month = monthsShort[dateTime.date.monthNumber - 1]
    val year = dateTime.date.year
    val hour = dateTime.time.hour
    val minute = dateTime.time.minute

    // Determine AM/PM and format hour (12-hour clock)
    val ampm = if (hour < 12) "AM" else "PM"
    val hour12 = if (hour % 12 == 0) 12 else hour % 12
    val minuteFormatted = if (minute < 10) "0$minute" else minute.toString()

    // Construct the formatted string
    return "$day $month $year ${hour12}:${minuteFormatted} $ampm"
}

fun getTimePeriodUnit(month: Int): String {
    val result = if (month in 0..11) {
        month.toDouble()  // Convert month to Double for fractional support
    } else {
        month / 12.0  // Divide by 12 to get the number of years as a double
    }
    val roundedResult = (result * 10.0).roundToInt() / 10.0

    return roundedResult.toString()  // Format the result to 1 decimal point
}

suspend fun formatDateRange(
    start: Instant,
    end: Instant,
    zoneId: TimeZone = TimeZone.currentSystemDefault()
): String {
    // Convert Instant to LocalDate in the provided TimeZone
    val startDate = start.toLocalDateTime(zoneId).date
    val endDate = end.toLocalDateTime(zoneId).date

    val monthsShort = getStringArray(Res.array.months_short)

    // Extract day, month, and year from start and end dates
    val startDay = startDate.dayOfMonth
    val startMonth = monthsShort[startDate.monthNumber - 1]
    val startYear = startDate.year

    val endDay = endDate.dayOfMonth
    val endMonth = monthsShort[endDate.monthNumber - 1]
    val endYear = endDate.year

    // Return the formatted range string
    return "$startDay $startMonth - $endDay $endMonth $startYear"
}

@Composable
fun getTimePeriod(month: Int): String {
    return if (month == 0 || month in 2..11) {
        stringResource(Res.string.time_period_months)
    } else if (month == 1) {
        stringResource(Res.string.time_period_month)
    } else if (month == 12) {
        stringResource(Res.string.time_period_year)
    } else if (month > 12) {
        stringResource(Res.string.time_period_years)
    } else {
        ""
    }
}

fun formatMoney(amount: Int): String {
    val amountString = amount.toString()
    val formatted = buildString {
        // Reverse the string to process from the end
        val reversed = amountString.reversed()
        for (i in reversed.indices) {
            if (i > 0 && i % 3 == 0) append(",") // Add commas every 3 digits
            append(reversed[i])
        }
    }.reversed() // Reverse back to the correct order
    return "$$formatted.00" // Add the dollar sign and decimal part
}

/**
 * 2026-08-20 (screens 1-30 re-verification pass): `TripModel.price` is a real `Double` (unlike
 * the payment-flow screens' amounts, which all originate from an already-whole-dollar `Int`
 * route/state field and only pass through `Double` in transit) -- `TripItem.kt`/`WishItem.kt`
 * were calling `formatMoney(item.price.toInt())`, silently truncating any real cents (e.g. 49.99
 * displayed as "$49.00"). This overload rounds to the nearest cent instead of truncating to the
 * nearest whole dollar.
 */
fun formatMoney(amount: Double): String {
    val totalCents = (amount * 100).roundToInt()
    val integerPart = totalCents / 100
    val decimalPart = kotlin.math.abs(totalCents % 100)
    val formattedInteger = integerPart.toString().reversed().chunked(3).joinToString(",").reversed()
    return "$$formattedInteger.${decimalPart.toString().padStart(2, '0')}"
}

fun formatPhone(phone: String): String {
    if (phone.length != 10 || phone.any { !it.isDigit() }) {
        return "XXX-XXX-XXXX"
        //throw IllegalArgumentException("Phone number must be exactly 10 digits.")
    }
    return "${phone.substring(0, 3)}-${phone.substring(3, 6)}-${phone.substring(6)}"
}


suspend fun timeUntil(targetDate: Instant): String {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val targetLocalDate = targetDate.toLocalDateTime(TimeZone.UTC).date

    return when {
        targetLocalDate == currentDate -> getString(Res.string.time_today)
        targetLocalDate > currentDate -> {
            val totalDays = currentDate.daysUntil(targetLocalDate)
            val months = totalDays / 30 // Aproximación
            val days = totalDays % 30

            when {
                months > 0 -> getString(
                    if (months > 1) Res.string.time_months_other else Res.string.time_months_one,
                    months
                )
                days > 0 -> getString(
                    if (days > 1) Res.string.time_days_other else Res.string.time_days_one,
                    days
                )
                else -> getString(Res.string.time_today)
            }
        }

        else -> getString(Res.string.time_past_date)
    }
}

fun localDateToInstant(localDate: LocalDate, timeZone: TimeZone): Instant {
    val localDateTime = localDate.atStartOfDayIn(timeZone)
    return localDateTime
}

fun formatDouble(value: Double, decimals: Int = 1): String {
    val factor = 10.0.pow(decimals)
    val rounded = (value * factor).roundToLong() / factor
    return rounded.toString()
}


val URL: String get() = ApiConfig.baseUrl

