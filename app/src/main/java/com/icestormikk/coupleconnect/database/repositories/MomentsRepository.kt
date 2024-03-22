package com.icestormikk.coupleconnect.database.repositories

import androidx.lifecycle.LiveData
import com.icestormikk.coupleconnect.database.daos.MomentDao
import com.icestormikk.coupleconnect.database.entities.moments.Moment
import java.time.LocalDate

class MomentsRepository(private val momentDao: MomentDao) {
    val readAllData = momentDao.getAll()

    suspend fun getMomentById(id: Long): Moment? {
        return momentDao.getById(id)
    }

    suspend fun addMoment(moment: Moment): Long {
        return momentDao.insert(moment)
    }

    suspend fun updateMoment(moment: Moment) {
        return momentDao.update(moment)
    }

    suspend fun deleteMoment(moment: Moment) {
        return momentDao.delete(moment)
    }
}