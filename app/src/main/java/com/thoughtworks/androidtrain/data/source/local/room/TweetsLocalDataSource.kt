package com.thoughtworks.androidtrain.data.source.local.room

import com.thoughtworks.androidtrain.data.source.local.room.dao.TweetDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.flow.Flow

class TweetsLocalDataSource(
    private val tweetDao: TweetDao,
) {
    fun getTweetsStream(): Flow<List<TweetPO>> {
        return tweetDao.observeTweets()
    }

    fun addTweet(tweetPO: TweetPO): Long {
        return tweetDao.insertTweet(tweetPO)
    }

    fun getTweetWithSender(): Flow<List<TweetWithSenderAndCommentsAndImages>> {
        return tweetDao.observeTweetsMap()
    }
}