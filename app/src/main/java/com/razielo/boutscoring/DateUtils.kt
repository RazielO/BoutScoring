package com.razielo.boutscoring

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateUtils {
    companion object {
        var verboseFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        var isoFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

        fun millisToLocalDate(millis: Long): LocalDate {
            return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDate()
        }

        fun stringToMillis(string: String?, formatter: DateTimeFormatter): Long? {
            return try {
                LocalDate.parse(string, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()
            } catch (e: Exception) {
                null
            }
        }

        fun changeFormatter(
            string: String?, from: DateTimeFormatter, to: DateTimeFormatter
        ): String? {
            val millis: Long? = stringToMillis(string, from)
            val date: LocalDate? = millis?.let { millisToLocalDate(it) }
            return date?.let { dateToString(it, to) }
        }

        fun millisToLocalDateWithFormatter(
            date: LocalDate, dateTimeFormatter: DateTimeFormatter
        ): LocalDate {
            val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            return Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        }

        fun dateToString(
            date: LocalDate, dateFormatter: DateTimeFormatter = verboseFormatter
        ): String {
            val dateInMillis = millisToLocalDateWithFormatter(date, dateFormatter)
            return dateFormatter.format(dateInMillis)
        }
    }
}
