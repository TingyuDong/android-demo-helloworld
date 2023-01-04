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

class TweetsRemoteDataSource(
    private val tweetsApiImpl: TweetsApiHelperImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    private var observableTweets = MutableStateFlow<Result<List<Tweet>>>(Loading)

    fun getTweetsStream(): Flow<Result<List<Tweet>>> {
        return observableTweets
    }

    private suspend fun getTweets() {
        tweetsApiImpl.getTweets()
            .flowOn(ioDispatcher)
            .catch { e ->
                observableTweets.value = Error(e as Exception)
            }.collect { tweetList ->
                observableTweets.value = Success(tweetList)
            }
    }

    suspend fun refreshTweets() {
        getTweets()
    }
}
