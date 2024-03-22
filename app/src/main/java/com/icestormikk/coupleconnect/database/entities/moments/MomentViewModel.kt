package com.icestormikk.coupleconnect.database.entities.moments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.icestormikk.coupleconnect.database.ApplicationDatabase
import com.icestormikk.coupleconnect.database.repositories.ImagesRepository
import com.icestormikk.coupleconnect.database.repositories.MomentsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

class MomentViewModel(application: Application): AndroidViewModel(application) {
    private val _viewModelJob = Job()
    private val _uiScope = CoroutineScope(Dispatchers.Main + _viewModelJob)
    private val _repository: MomentsRepository
    val allData: LiveData<List<Moment>>

    init {
        val momentsDao = ApplicationDatabase.getInstance(application).getMomentsDao()
        _repository = MomentsRepository(momentsDao)
        allData = _repository.readAllData
    }

    fun getById(id: Long): Deferred<Moment?> {
        return _uiScope.async {
            _repository.getMomentById(id)
        }
    }

    fun insert(moment: Moment): Deferred<Long> {
        return _uiScope.async {
            _repository.addMoment(moment)
        }
    }

    fun update(moment: Moment) {
        _uiScope.launch {
            _repository.updateMoment(moment)
        }
    }

    fun delete(moment: Moment) {
        _uiScope.launch {
            _repository.deleteMoment(moment)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _viewModelJob.cancel()
    }
}