package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.source.local.room.dao.TweetDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource

interface TweetRepositoryInterface {
    fun getAllLocalTweets(): List<TweetPO>
    suspend fun getAllRemoteTweets(): List<Tweet>
    fun addTweet(tweetPO: TweetPO): Long
}

class TweetRepository(
    private val tweetDao: TweetDao,
    private val tweetsRemoteDataSource: TweetsRemoteDataSource
) : TweetRepositoryInterface {
    override fun getAllLocalTweets(): List<TweetPO> {
        return tweetDao.getAll()
    }

    override suspend fun getAllRemoteTweets(): List<Tweet> {
        return tweetsRemoteDataSource.fetchRemoteTweets()
    }

    override fun addTweet(tweetPO: TweetPO): Long {
        return tweetDao.insertTweet(tweetPO)
    }
}