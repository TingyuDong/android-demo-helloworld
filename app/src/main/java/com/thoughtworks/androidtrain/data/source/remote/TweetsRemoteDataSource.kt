package com.thoughtworks.androidtrain.data.source.remote

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.source.TweetsDataSource
import com.thoughtworks.androidtrain.service.TweetService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.IOException

class TweetsRemoteDataSource (
    private val ioDispatcher: CoroutineDispatcher,
) : TweetsDataSource {
    private val observableTweets = MutableStateFlow(runBlocking { getTweets() })

    suspend fun fetchRemoteTweets(): Result<List<Tweet>> {
        val service = RetrofitClientInstance.getRetrofitInstance().create(TweetService::class.java)
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

    override fun getTweetsStream(): Flow<Result<List<Tweet>>> {
        return observableTweets
    }

    override suspend fun getTweets(): Result<List<Tweet>> {
        val service = RetrofitClientInstance.getRetrofitInstance().create(TweetService::class.java)
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

    override suspend fun refreshTasks() {
        observableTweets.value = getTweets()
    }
}
