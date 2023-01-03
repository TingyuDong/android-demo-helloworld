package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments")
    fun observeComments(): Flow<List<CommentPO>>

    @Query("SELECT * FROM comments")
    fun getAllComments(): List<CommentPO>

    @Query("SELECT * FROM comments WHERE tweet_id =:tweetId")
    suspend fun getCommentsByTweetId(tweetId: Int): List<CommentPO>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComments(commentsPO: List<CommentPO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(commentPO: CommentPO): Long
}