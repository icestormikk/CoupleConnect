package com.icestormikk.coupleconnect.database.entities.moments

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.core.content.res.ResourcesCompat
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.icestormikk.coupleconnect.utilities.converters.LocalDateTimeConverter
import com.icestormikk.coupleconnect.utilities.converters.MomentTypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime

data class Coordinate(
    val latitude: Double,
    val longitude: Double,
)

@Parcelize
@Entity(tableName = "moments")
@TypeConverters(LocalDateTimeConverter::class, MomentTypeConverter::class)
data class Moment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime,

    @ColumnInfo(name = "title")
    val title: String,

    @Embedded
    val address: @RawValue Coordinate,

    @ColumnInfo(name = "note")
    val note: String,

    @ColumnInfo(name = "type")
    val type: MomentType,

    @ColumnInfo(name = "relationships_id")
    val relationshipsId: Long
) : Parcelable {
    fun getIconByType(resources: Resources): Drawable? {
        return ResourcesCompat.getDrawable(resources, type.iconId, null)
    }
}
