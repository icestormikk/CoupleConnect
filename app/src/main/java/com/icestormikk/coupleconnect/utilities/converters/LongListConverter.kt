package com.icestormikk.coupleconnect.utilities.converters

import androidx.room.TypeConverter

class LongListConverter {
    @TypeConverter
    fun fromList(source: List<Long>): String {
        return source.joinToString(";")
    }

    @TypeConverter
    fun toList(source: String): List<Long> {
        return source.split(";")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
    }
}