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
    @Query("SELECT * FROM tweets")
    fun observeTweets(): Flow<List<TweetPO>>

    @Query("SELECT * FROM tweets")
    fun getAll(): List<TweetPO>

    @Query("SELECT * FROM tweets where id =:tweetId")
    fun getTweet(tweetId: Int): TweetPO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTweet(tweet: TweetPO): Long

    @Transaction
    @Query("SELECT * FROM tweets")
    fun observeTweetsMap(): Flow<List<TweetWithSenderAndCommentsAndImages>>
}