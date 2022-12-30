package com.thoughtworks.androidtrain.data.source.remote

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.service.TweetService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.IOException

class TweetsRemoteDataSource (
    private val ioDispatcher: CoroutineDispatcher,
    private val service: TweetService
) {
    private val observableTweets = MutableStateFlow(runBlocking { getTweets() })

    fun getTweetsStream(): Flow<Result<List<Tweet>>> {
        return observableTweets
    }

    private suspend fun getTweets(): Result<List<Tweet>> {
        return withContext(ioDispatcher) {
            try {
                val response = service.listTweets().execute()
                if (response.isSuccessful) {
                    return@withContext Result.success(response.body() ?: emptyList())
                } else {
                    return@withContext Result.failure(exception = Exception(response.message()))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext Result.failure(exception = e)
            }
        }
    }

    suspend fun refreshTasks() {
        observableTweets.value = getTweets()
    }
}
