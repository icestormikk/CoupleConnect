package com.icestormikk.coupleconnect.database.repositories

import androidx.lifecycle.LiveData
import com.icestormikk.coupleconnect.database.daos.RelationshipsDao
import com.icestormikk.coupleconnect.database.entities.relationships.Relationships
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithMoments
import com.icestormikk.coupleconnect.database.entities.relationships.RelationshipsWithPhotos

class RelationshipsRepository(private val relationshipsDao: RelationshipsDao) {
    fun getRelationships() = relationshipsDao.getAllRelationships()

    fun getRelationshipWithPhotos() = relationshipsDao.getAllRelationshipsWithPhotos()
    suspend fun getRelationshipsWithPhotosById(id: Long) = relationshipsDao.getRelationshipsWithPhotosById(id)

    fun getRelationshipsWithMoments() = relationshipsDao.getAllRelationshipsWithMoments()
    suspend fun getRelationshipsWithMomentsById(id: Long) = relationshipsDao.getRelationshipsWithMomentsById(id)

    suspend fun addRelationships(relationships: Relationships): Long {
        return relationshipsDao.insertRelationships(relationships)
    }

    suspend fun removeRelationships(relationships: Relationships) {
        return relationshipsDao.deleteRelationships(relationships)
    }

    suspend fun updateRelationships(relationships: Relationships) {
        return relationshipsDao.updateRelationships(relationships)
    }
}