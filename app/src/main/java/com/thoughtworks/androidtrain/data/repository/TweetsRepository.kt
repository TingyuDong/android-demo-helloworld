package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.source.local.room.TweetsLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import kotlinx.coroutines.flow.Flow

interface TweetsRepositoryInterface {
    fun getAllLocalTweets(): List<TweetPO>
    fun getRemoteTweetsStream(): Flow<Result<List<Tweet>>>
    fun getLocalTweetsStream(): Flow<List<TweetPO>>
    suspend fun refreshTweets()
    fun addTweet(tweetPO: TweetPO): Long
}

class TweetsRepository(
    private val tweetsRemoteDataSource: TweetsRemoteDataSource,
    private val tweetsLocalDataSource: TweetsLocalDataSource
) : TweetsRepositoryInterface {
    override fun getAllLocalTweets(): List<TweetPO> {
        return tweetsLocalDataSource.getTweets()
    }

    override fun getRemoteTweetsStream(): Flow<Result<List<Tweet>>> {
        return tweetsRemoteDataSource.getTweetsStream()
    }

    override fun getLocalTweetsStream(): Flow<List<TweetPO>> {
        return tweetsLocalDataSource.getTweetsStream()
    }

    override suspend fun refreshTweets() {
        tweetsRemoteDataSource.refreshTweets()
    }

    override fun addTweet(tweetPO: TweetPO): Long {
        return tweetsLocalDataSource.addTweet(tweetPO)
    }
}