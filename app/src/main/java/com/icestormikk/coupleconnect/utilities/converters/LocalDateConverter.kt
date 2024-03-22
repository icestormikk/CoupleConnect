package com.icestormikk.coupleconnect.utilities.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    private val _formatter = DateTimeFormatter.ISO_DATE_TIME

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.format(_formatter)
    }

    @TypeConverter
    fun toDate(source: String): LocalDate {
        return LocalDate.parse(source, _formatter)
    }
}