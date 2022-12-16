package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.source.local.room.TweetsLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.dao.TweetDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import kotlinx.coroutines.flow.Flow

interface TweetRepositoryInterface {
    fun getAllLocalTweets(): List<TweetPO>
    fun getRemoteTweetsStream(): Flow<Result<List<Tweet>>>
    fun getLocalTweetsStream(): Flow<Result<List<TweetPO>>>
    suspend fun refreshTweets()
    fun addTweet(tweetPO: TweetPO): Long
}

class TweetRepository(
    private val tweetDao: TweetDao,
    private val tweetsRemoteDataSource: TweetsRemoteDataSource,
    private val tweetsLocalDataSource: TweetsLocalDataSource
) : TweetRepositoryInterface {
    override fun getAllLocalTweets(): List<TweetPO> {
        return tweetDao.getAll()
    }

    override fun getRemoteTweetsStream(): Flow<Result<List<Tweet>>> {
        return tweetsRemoteDataSource.getTweetsStream()
    }

    override fun getLocalTweetsStream(): Flow<Result<List<TweetPO>>> {
        return tweetsLocalDataSource.getTweetsStream()
    }

    override suspend fun refreshTweets() {
        tweetsRemoteDataSource.refreshTasks()
    }

    override fun addTweet(tweetPO: TweetPO): Long {
        return tweetDao.insertTweet(tweetPO)
    }
}