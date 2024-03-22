package com.icestormikk.coupleconnect.utilities.converters

import androidx.room.TypeConverter
import com.icestormikk.coupleconnect.database.entities.moments.MomentType

class MomentTypeConverter {
    @TypeConverter
    fun fromMomentType(type: MomentType) = type.name

    @TypeConverter
    fun toMomentType(name: String) = enumValueOf<MomentType>(name)
}