package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSender
import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSender.Companion.sender_prefix
import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSender.Companion.tweet_prefix
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

    @Query(
        "SELECT tweets.id as $tweet_prefix" + "id, " +
                "tweets.content as $tweet_prefix" + "content, " +
                "tweets.sender_name as $tweet_prefix" + "sender_name, " +
                "tweets.error as $tweet_prefix" + "error, " +
                "tweets.unknown_error as $tweet_prefix" + "unknown_error, " +
                "sender.user_name as $sender_prefix" + "user_name," +
                "sender.nick as $sender_prefix" + "nick," +
                "sender.avatar as $sender_prefix" + "avatar " +
                "FROM tweets INNER JOIN sender where tweets.sender_name = sender.user_name"
    )
    fun observeTweetsMap(): Flow<List<TweetWithSender>>
}