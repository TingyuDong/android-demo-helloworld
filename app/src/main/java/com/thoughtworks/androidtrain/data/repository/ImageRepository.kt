package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import java.util.stream.Collectors

class ImageRepository(repository: Repository) : ImageRepositoryInterface {
    private val database: AppDatabase = repository.getDatabase()

    private val imageDao = database.imageDao()

    override fun getImages(tweetId: Int): List<Image>? {
        val imagesPO = imageDao.getImages(tweetId)
        if (imagesPO != null) {
            return imagesPO.stream().map { Image(it.id, it.url) }.collect(Collectors.toList())
        }
        return null
    }

    override fun addImages(images: List<Image>?, tweetId: Int) {
        val imagesCollect = images?.stream()?.map {
            if (it.id == null) {
                ImagePO(0, tweetId, it.url)
            } else {
                ImagePO(it.id, tweetId, it.url)
            }
        }?.collect(Collectors.toList())
        if (imagesCollect != null) {
            imageDao.insertAllImages(imagesCollect)
        }
    }
}