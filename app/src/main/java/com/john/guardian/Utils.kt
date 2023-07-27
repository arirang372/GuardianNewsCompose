package com.john.guardian

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.Locale


fun String.toDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss'Z'",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date {
    var input = this
    return SimpleDateFormat(dateFormat, Locale.getDefault()).apply {
        this.timeZone = timeZone
    }.run {
        parse(input)
    }
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    var input: Date = this
    return SimpleDateFormat(dateFormat, Locale.getDefault()).apply {
        this.timeZone = timeZone
    }.run {
        format(input)
    }
}
