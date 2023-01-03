package com.thoughtworks.androidtrain.data.source.local.room

import com.thoughtworks.androidtrain.data.source.local.room.dao.ImageDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import kotlinx.coroutines.flow.Flow

class ImagesLocalDataSource(
    private val imageDao: ImageDao
) {
    fun getImagesStream(): Flow<List<ImagePO>> {
        return imageDao.observeImages()
    }

    suspend fun getImages(tweetId: Int): List<ImagePO>? {
        return imageDao.getImages(tweetId)
    }

    suspend fun addImages(imagePOList: List<ImagePO>) {
        return imageDao.insertAllImages(imagePOList)
    }
}