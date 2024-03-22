package com.icestormikk.coupleconnect.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.icestormikk.coupleconnect.database.daos.ImageDao
import com.icestormikk.coupleconnect.database.daos.MomentDao
import com.icestormikk.coupleconnect.database.daos.RelationshipsDao
import com.icestormikk.coupleconnect.database.entities.image.Image
import com.icestormikk.coupleconnect.database.entities.moments.Moment
import com.icestormikk.coupleconnect.database.entities.relationships.Relationships

@Database(
    entities = [Relationships::class, Image::class, Moment::class],
    version = 16,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract fun getRelationshipsDao(): RelationshipsDao
    abstract fun getImageDao(): ImageDao
    abstract fun getMomentsDao(): MomentDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getInstance(context: Context): ApplicationDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            ApplicationDatabase::class.java,
                            "relationships.db"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}