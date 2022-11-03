package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.Image

@Dao
interface ImageDao {
    @Query("SELECT * FROM image where tweet_id =:tweetId")
    suspend fun getImage(tweetId: Int): List<Image>?
}