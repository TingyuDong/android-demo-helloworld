package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments")
    fun getAll(): List<CommentPO>

    @Query("SELECT * FROM comments where tweet_id =:tweetId")
    fun getComments(tweetId: Int): List<CommentPO>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllComments(commentsPO: List<CommentPO>)
}