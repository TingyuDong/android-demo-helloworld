package com.thoughtworks.androidtrain.data.source.remote

import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success
import com.thoughtworks.androidtrain.data.Result.Error
import com.thoughtworks.androidtrain.data.Result.Loading
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.service.TweetsApiHelperImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.MutableStateFlow

interface TweetsRemoteDataSource {
    fun getTweetsStream(): Flow<Result<List<Tweet>>>
    suspend fun getTweets()
    suspend fun refreshTweets()
}

open class TweetsRemoteDataSourceImpl(
    private val tweetsApiImpl: TweetsApiHelperImpl,
    private val ioDispatcher: CoroutineDispatcher
) : TweetsRemoteDataSource {
    private var observableTweets = MutableStateFlow<Result<List<Tweet>>>(Loading)

    override fun getTweetsStream(): Flow<Result<List<Tweet>>> {
        return observableTweets
    }

    override suspend fun getTweets() {
        tweetsApiImpl.getTweets()
            .flowOn(ioDispatcher)
            .catch { e ->
                observableTweets.value = Error(e as Exception)
            }.collect { tweetList ->
                observableTweets.value = Success(tweetList)
            }
    }

    override suspend fun refreshTweets() {
        getTweets()
    }
}
