package com.thoughtworks.androidtrain.data.source.remote

import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success
import com.thoughtworks.androidtrain.data.Result.Error
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.service.TweetService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.IOException

class TweetsRemoteDataSource(
    private val service: TweetService,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val observableTweets = MutableStateFlow(runBlocking { getTweets() })

    fun getTweetsStream(): Flow<Result<List<Tweet>>> {
        return observableTweets
    }

    private suspend fun getTweets(): Result<List<Tweet>> = withContext(ioDispatcher) {
        return@withContext try {
            val response = service.listTweets().execute()
            if (response.isSuccessful) {
                Success(response.body() ?: emptyList())
            } else {
                Error(exception = Exception(response.message()))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Error(exception = e)
        }
    }

    suspend fun refreshTweets() {
        observableTweets.value = getTweets()
    }
}
