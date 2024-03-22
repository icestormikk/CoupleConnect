package com.icestormikk.coupleconnect.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.icestormikk.coupleconnect.database.entities.image.Image

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image): Long

    @Insert
    suspend fun insert(images: List<Image>): List<Long>

    @Delete()
    suspend fun delete(image: Image)

    @Query("SELECT * FROM images ORDER BY id ASC")
    fun getAll(): LiveData<List<Image>>

    @Query("SELECT * FROM images WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Image?

    @Query("SELECT * FROM images WHERE id IN (:idList)")
    suspend fun getByIds(idList: List<Long>): List<Image>
}