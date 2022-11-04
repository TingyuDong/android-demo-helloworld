package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Image

interface ImageRepositoryInterface {
    fun getImages(tweetId: Int): List<Image>?
    fun addImages(images: List<Image>?, tweetId: Int)
}