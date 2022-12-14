package com.thoughtworks.androidtrain.data.source

import com.thoughtworks.androidtrain.data.model.Tweet
import kotlinx.coroutines.flow.Flow

interface TweetsDataSource {
    fun getTweetsStream(): Flow<Result<List<Tweet>>>

    suspend fun getTweets(): Result<List<Tweet>>

    suspend fun refreshTasks()
}