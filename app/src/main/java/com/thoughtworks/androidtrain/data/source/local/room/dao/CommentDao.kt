package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.Comment

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments")
    fun getAll(): List<Comment>

    @Query("SELECT * FROM comments where tweet_id =:tweetId")
    suspend fun getComment(tweetId: Int): List<Comment>?
}