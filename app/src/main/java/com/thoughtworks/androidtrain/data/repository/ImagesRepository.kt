package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.source.local.room.ImagesLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO

interface ImagesRepository {
    suspend fun addImages(images: List<Image>, tweetId: Int)
    fun transformToImageList(imagePOList: List<ImagePO>): List<Image>
}

class ImagesRepositoryImpl(
    private val imageDataSource: ImagesLocalDataSource
) : ImagesRepository {
    override suspend fun addImages(images: List<Image>, tweetId: Int) {
        imageDataSource.addImages(images.map {
            ImagePO(id = 0, tweetId = tweetId, url = it.url)
        })
    }

    override fun transformToImageList(imagePOList: List<ImagePO>): List<Image> {
        return imagePOList.map { transformToImage(it) }
    }

    private fun transformToImage(imagePO: ImagePO): Image {
        return Image(url = imagePO.url)
    }
}