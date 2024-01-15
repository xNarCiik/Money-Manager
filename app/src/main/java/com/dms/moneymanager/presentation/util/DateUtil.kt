package com.dms.moneymanager.presentation.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.monthlyAndYearString() = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(this)

fun getCurrentDateString(): String = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

fun getLastDayOfMonthDateString(): String = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(getLastDayOfMonthDate())

fun getDayOfTheMonth(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun getLastDayOfMonthDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    return calendar.time
}