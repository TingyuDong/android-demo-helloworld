package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO

@Dao
interface TweetDao {
    @Query("SELECT * FROM tweets")
    fun getAll(): List<TweetPO>

    @Query("SELECT * FROM tweets "+
            "JOIN comments ON tweets.id = comments.tweet_id ")
    fun getAllTweetWithComment(): Map<TweetPO,List<CommentPO>>

    @Query("SELECT * FROM sender where id =:tweetId")
    fun getTweet(tweetId: Int): TweetPO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTweet(tweet: TweetPO): Long
}