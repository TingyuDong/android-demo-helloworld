package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO

@Dao
interface ImageDao {
    @Query("SELECT * FROM image where tweet_id =:tweetId")
    fun getImages(tweetId: Int): List<ImagePO>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllImages(commentsPO: List<ImagePO>)
}