package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.thoughtworks.androidtrain.data.model.Image

@Dao
interface ImageDao {
    @Query("SELECT * FROM image where tweet_id =:tweetId")
    fun getImages(tweetId: Int): List<Image>?
}