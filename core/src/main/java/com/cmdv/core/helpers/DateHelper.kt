package com.cmdv.core.helpers

import com.cmdv.core.Constants
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun getDayMonthYearWithBars(dateString: String, formatOrigin: SimpleDateFormat): String {
    var dayMonthYear = ""
    try {
        val d: Date?  = formatOrigin.parse(dateString)
        val f = SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YY_BAR, Locale.getDefault())
        dayMonthYear = f.format(d)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        return dayMonthYear
    }
}

fun getHoursMinutes(dateString: String, formatOrigin: SimpleDateFormat): String {
    var hoursMinutes = ""
    try {
        val d: Date?  = formatOrigin.parse(dateString)
        val f = SimpleDateFormat(Constants.DATE_FORMAT_HH_MM, Locale.getDefault())
        hoursMinutes = f.format(d)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        return hoursMinutes
    }
}