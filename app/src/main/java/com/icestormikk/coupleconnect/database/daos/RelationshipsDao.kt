package com.icestormikk.coupleconnect.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.icestormikk.coupleconnect.database.entities.relationships.Relationships
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithMoments
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithPhotos

@Dao
interface RelationshipsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelationships(relationships: Relationships): Long

    @Delete
    suspend fun deleteRelationships(relationships: Relationships)

    @Update
    suspend fun updateRelationships(relationships: Relationships)

    @Query("SELECT * FROM relationships ORDER BY id ASC")
    fun getAllRelationships(): LiveData<List<Relationships>>

    @Transaction
    @Query("SELECT * FROM relationships ORDER BY id ASC")
    fun getAllRelationshipsWithPhotos(): LiveData<List<RelationshipsWithPhotos>>

    @Transaction
    @Query("SELECT * FROM relationships WHERE id = :id LIMIT 1")
    suspend fun getRelationshipsWithPhotosById(id: Long): RelationshipsWithPhotos?

    @Transaction
    @Query("SELECT * FROM relationships ORDER BY id ASC")
    fun getAllRelationshipsWithMoments(): LiveData<List<RelationshipsWithMoments>>

    @Transaction
    @Query("SELECT * FROM relationships WHERE id = :id LIMIT 1")
    suspend fun getRelationshipsWithMomentsById(id: Long): RelationshipsWithMoments?
}