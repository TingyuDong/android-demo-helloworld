package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSenderAndCommentsAndImages
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetDao {
    @Transaction
    @Query("SELECT * FROM tweet")
    fun observeTweets(): Flow<List<TweetWithSenderAndCommentsAndImages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTweet(tweet: TweetPO): Long
}