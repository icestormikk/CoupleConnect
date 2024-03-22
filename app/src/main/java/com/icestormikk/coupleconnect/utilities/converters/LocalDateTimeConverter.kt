package com.icestormikk.coupleconnect.utilities.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
    private val _formatter = DateTimeFormatter.ISO_DATE_TIME

    @TypeConverter
    fun fromDate(date: LocalDateTime): String {
        return date.format(_formatter)
    }

    @TypeConverter
    fun toDate(source: String): LocalDateTime {
        return LocalDateTime.parse(source, _formatter)
    }
}