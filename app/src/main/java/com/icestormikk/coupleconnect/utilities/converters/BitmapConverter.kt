package com.icestormikk.coupleconnect.utilities.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapConverter {
    @TypeConverter
    fun fromBitmap(source: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        source.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(source: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(source, 0, source.size)
    }
}