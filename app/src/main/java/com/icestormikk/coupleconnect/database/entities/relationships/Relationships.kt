package com.icestormikk.coupleconnect.database.entities.relationships

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.icestormikk.coupleconnect.database.entities.image.Image
import com.icestormikk.coupleconnect.database.entities.moments.Moment
import com.icestormikk.coupleconnect.utilities.converters.LocalDateTimeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime

@Parcelize
@TypeConverters(LocalDateTimeConverter::class)
@Entity(tableName = "relationships")
data class Relationships(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "owner_name")
    val ownerName: String,

    @ColumnInfo(name = "partner_name")
    val partnerName: String,

    @ColumnInfo(name = "start_date_time")
    val startDateTime: LocalDateTime
) : Parcelable

@Parcelize
data class RelationshipsWithPhotos(
    @Embedded
    val relationships: Relationships,
    @Relation(parentColumn = "id", entityColumn = "relationships_id")
    val photos: @RawValue List<Image>
) : Parcelable

@Parcelize
data class RelationshipsWithMoments(
    @Embedded
    val relationships: Relationships,
    @Relation(parentColumn = "id", entityColumn = "relationships_id")
    val moments: @RawValue List<Moment>
) : Parcelable