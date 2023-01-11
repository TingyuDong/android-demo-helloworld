package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.source.local.room.ImagesLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun getImagesStream(): Flow<List<ImagePO>>
    suspend fun getImages(tweetId: Int): List<Image>?
    suspend fun addImages(images: List<Image>, tweetId: Int)
}

class ImagesRepositoryImpl(
    private val imageDataSource: ImagesLocalDataSource
) : ImagesRepository {
    override fun getImagesStream(): Flow<List<ImagePO>> {
        return imageDataSource.getImagesStream()
    }

    override suspend fun getImages(tweetId: Int): List<Image>? {
        return imageDataSource.getImages(tweetId)?.map { transformToImage(it) }
    }

    override suspend fun addImages(images: List<Image>, tweetId: Int) {
        imageDataSource.addImages(images.map {
            ImagePO(id = 0, tweetId = tweetId, url = it.url)
        })
    }

    private fun transformToImage(imagePO: ImagePO): Image {
        return Image(url = imagePO.url)
    }
}