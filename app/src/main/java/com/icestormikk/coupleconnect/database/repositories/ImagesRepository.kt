package com.icestormikk.coupleconnect.database.repositories

import androidx.lifecycle.LiveData
import com.icestormikk.coupleconnect.database.daos.ImageDao
import com.icestormikk.coupleconnect.database.entities.image.Image

class ImagesRepository(private val imageDao: ImageDao) {
    val readAllData = imageDao.getAll()

    suspend fun getImageById(id: Long): Image? {
        return imageDao.getById(id)
    }

    suspend fun getImagesByIds(idList: List<Long>): List<Image> {
        return imageDao.getByIds(idList)
    }

    suspend fun uploadImage(image: Image): Long {
        return imageDao.insert(image)
    }

    suspend fun uploadManyImages(images: List<Image>): List<Long> {
        return imageDao.insert(images)
    }
}