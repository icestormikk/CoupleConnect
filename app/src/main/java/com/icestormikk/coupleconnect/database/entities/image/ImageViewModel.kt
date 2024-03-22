package com.icestormikk.coupleconnect.database.entities.image

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.icestormikk.coupleconnect.database.ApplicationDatabase
import com.icestormikk.coupleconnect.database.repositories.ImagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class ImageViewModel(application: Application): AndroidViewModel(application) {
    private var _viewModelJob = Job()
    private val _uiScope = CoroutineScope(Dispatchers.Main + _viewModelJob)
    private val _repository: ImagesRepository
    val allData: LiveData<List<Image>>

    init {
        val imagesDao = ApplicationDatabase.getInstance(application).getImageDao()
        _repository = ImagesRepository(imagesDao)
        allData = _repository.readAllData
    }

    suspend fun getImageById(id: Long): Image? {
        return _repository.getImageById(id)
    }

    fun getImagesByIds(idList: List<Long>): Deferred<List<Image>> {
        return _uiScope.async {
            _repository.getImagesByIds(idList)
        }
    }

    fun insert(image: Image): Deferred<Long> {
        return _uiScope.async {
            _repository.uploadImage(image)
        }
    }

    fun insertMany(images: List<Image>): Deferred<List<Long>> {
        return _uiScope.async {
            _repository.uploadManyImages(images)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _viewModelJob.cancel()
    }
}