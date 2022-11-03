package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.Comment
import com.thoughtworks.androidtrain.data.source.local.room.entity.Image
import com.thoughtworks.androidtrain.data.source.local.room.entity.Tweet

@Dao
interface TweetDao {
    @Query("SELECT * FROM tweets")
    fun getAll(): List<Tweet>

    @Query("SELECT * FROM tweets "+
            "JOIN comments ON tweets.id = comments.tweet_id ")
    fun getAllTweetWithComment(): Map<Tweet,List<Comment>>
}