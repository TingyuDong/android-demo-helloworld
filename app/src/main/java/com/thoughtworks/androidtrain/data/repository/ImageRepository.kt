package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.source.local.room.dao.ImageDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import kotlinx.coroutines.flow.Flow
import java.util.stream.Collectors

interface ImageRepositoryInterface {
    fun getImagesStream(): Flow<List<ImagePO>>
    suspend fun getImages(tweetId: Int): List<Image>?
    fun addImages(images: List<Image>, tweetId: Int)
}

class ImageRepository(private val imageDao: ImageDao) : ImageRepositoryInterface {
    override fun getCommentsStream(): Flow<List<ImagePO>> {
        return imageDao.observeImages()
    }

    override suspend fun getImages(tweetId: Int): List<Image>? {
        return imageDao.getImages(tweetId)?.map { transformToImage(it) }
    }

    override fun addImages(images: List<Image>, tweetId: Int) {
        val imagesCollect = images.stream().map {
            ImagePO(id = 0, tweetId = tweetId, url = it.url)
        }?.collect(Collectors.toList())
        if (imagesCollect != null) {
            imageDao.insertAllImages(imagesCollect)
        }
    }

    private fun transformToImage(imagePO: ImagePO): Image {
        return Image(url = imagePO.url)
    }
}