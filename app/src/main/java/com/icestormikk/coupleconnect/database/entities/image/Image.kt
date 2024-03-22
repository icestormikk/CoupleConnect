package com.icestormikk.coupleconnect.database.entities.image

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.icestormikk.coupleconnect.utilities.converters.BitmapConverter

@Entity(tableName = "images")
@TypeConverters(BitmapConverter::class)
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "data")
    val data: Bitmap,

    @ColumnInfo(name = "relationships_id")
    val relationshipsId: Long
)
