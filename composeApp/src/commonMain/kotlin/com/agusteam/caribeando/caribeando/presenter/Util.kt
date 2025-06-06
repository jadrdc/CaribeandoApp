package com.agusteam.caribeando.presenter

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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

fun formatInstant(
    instant: Instant,
    zoneId: TimeZone = TimeZone.currentSystemDefault()
): String {
    // Convert Instant to LocalDateTime in the provided TimeZone
    val dateTime = instant.toLocalDateTime(zoneId)

    // Define the months in Spanish
    val monthsInSpanish = listOf(
        "Ene", "Feb", "Mar", "Abr", "May", "Jun",
        "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    )

    // Extract day, month, year, hour, and minute
    val day = dateTime.date.dayOfMonth
    val month = monthsInSpanish[dateTime.date.monthNumber - 1]
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

fun formatDateRange(
    start: Instant,
    end: Instant,
    zoneId: TimeZone = TimeZone.currentSystemDefault()
): String {
    // Convert Instant to LocalDate in the provided TimeZone
    val startDate = start.toLocalDateTime(zoneId).date
    val endDate = end.toLocalDateTime(zoneId).date

    // Define the months in Spanish manually
    val monthsInSpanish = listOf(
        "Ene", "Feb", "Mar", "Abr", "May", "Jun",
        "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    )

    // Extract day, month, and year from start and end dates
    val startDay = startDate.dayOfMonth
    val startMonth = monthsInSpanish[startDate.monthNumber - 1]
    val startYear = startDate.year

    val endDay = endDate.dayOfMonth
    val endMonth = monthsInSpanish[endDate.monthNumber - 1]
    val endYear = endDate.year

    // Return the formatted range string
    return "$startDay $startMonth - $endDay $endMonth $startYear"
}

fun getTimePeriod(month: Int): String {
    return if (month == 0 || month in 2..11) {
        "Meses"
    } else if (month == 1) {
        "Mes"
    } else if (month == 12) {
        "Año"
    } else if (month > 12) {
        "Años"
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

fun formatPhone(phone: String): String {
    if (phone.length != 10 || phone.any { !it.isDigit() }) {
        return "XXX-XXX-XXXX"
        //throw IllegalArgumentException("Phone number must be exactly 10 digits.")
    }
    return "${phone.substring(0, 3)}-${phone.substring(3, 6)}-${phone.substring(6)}"
}


fun timeUntil(targetDate: Instant): String {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val targetLocalDate = targetDate.toLocalDateTime(TimeZone.UTC).date

    return when {
        targetLocalDate == currentDate -> "Hoy"
        targetLocalDate > currentDate -> {
            val totalDays = currentDate.daysUntil(targetLocalDate)
            val months = totalDays / 30 // Approximation
            val days = totalDays % 7

            when {
                months > 0 -> "$months mes${if (months > 1) "eses" else ""} "
                else -> "$days dia${if (days > 1) "s" else ""} "
            }
        }

        else -> ""
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


const val URL = "https://caribeando-backend-production.up.railway.app/"

