package com.icestormikk.coupleconnect.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.icestormikk.coupleconnect.database.entities.moments.Moment

@Dao
interface MomentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moment: Moment): Long

    @Update
    suspend fun update(moment: Moment)

    @Delete
    suspend fun delete(moment: Moment)

    @Query("SELECT * FROM moments ORDER BY id")
    fun getAll(): LiveData<List<Moment>>

    @Query("SELECT * FROM moments WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Moment?
}