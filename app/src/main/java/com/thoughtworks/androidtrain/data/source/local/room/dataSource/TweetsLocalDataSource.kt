package com.thoughtworks.androidtrain.data.source.local.room.dataSource

import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSenderAndCommentsAndImages
import com.thoughtworks.androidtrain.data.source.local.room.dao.TweetDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.flow.Flow

interface TweetsLocalDataSource {
    fun getTweetsStream(): Flow<List<TweetWithSenderAndCommentsAndImages>>
    fun addTweet(tweetPO: TweetPO): Long
}

class TweetsLocalDataSourceImpl(
    private val tweetDao: TweetDao,
) : TweetsLocalDataSource {
    override fun getTweetsStream(): Flow<List<TweetWithSenderAndCommentsAndImages>> {
        return tweetDao.observeTweets()
    }

    override fun addTweet(tweetPO: TweetPO): Long {
        return tweetDao.insertTweet(tweetPO)
    }
}