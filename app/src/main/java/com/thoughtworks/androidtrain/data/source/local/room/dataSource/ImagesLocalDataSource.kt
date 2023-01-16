package com.thoughtworks.androidtrain.data.source.local.room.dataSource

import com.thoughtworks.androidtrain.data.source.local.room.dao.ImageDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO

class ImagesLocalDataSource(
    private val imageDao: ImageDao
) {
    suspend fun addImages(imagePOList: List<ImagePO>) {
        return imageDao.insertAllImages(imagePOList)
    }
}