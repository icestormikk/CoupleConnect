package com.icestormikk.coupleconnect.database.entities.relationships

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.icestormikk.coupleconnect.database.ApplicationDatabase
import com.icestormikk.coupleconnect.database.repositories.RelationshipsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RelationshipsViewModel(application: Application): AndroidViewModel(application) {
    private var _viewModelJob = Job()
    private val _uiScope = CoroutineScope(Dispatchers.Main + _viewModelJob)
    private val _repository: RelationshipsRepository

    init {
        val relationshipsDao = ApplicationDatabase.getInstance(application).getRelationshipsDao()
        _repository = RelationshipsRepository(relationshipsDao)
    }

    fun getAllRelationships() = _repository.getRelationships()

    fun getAllRelationshipsWithPhotos() = _repository.getRelationshipWithPhotos()
    fun getRelationshipsWithPhotosById(id: Long) =
        _uiScope.async {
            _repository.getRelationshipsWithPhotosById(id)
        }

    fun getAllRelationshipsWithMoments() = _repository.getRelationshipsWithMoments()
    fun getRelationshipsWithMomentsById(id: Long) =
        _uiScope.async {
            _repository.getRelationshipsWithMomentsById(id)
        }

    fun insert(relationships: Relationships) =
        _uiScope.async {
            _repository.addRelationships(relationships)
        }

    fun remove(relationships: Relationships) =
        _uiScope.async {
            _repository.removeRelationships(relationships)
        }

    fun update(relationships: Relationships) =
        _uiScope.async {
            _repository.updateRelationships(relationships)
        }

    override fun onCleared() {
        super.onCleared()
        _viewModelJob.cancel()
    }
}