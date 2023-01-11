package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.TweetsRepositoryImpl
import com.thoughtworks.androidtrain.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

open class FetchTweetsUseCase(
    private val tweetRepository: TweetsRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    open fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetRepository.getRemoteTweetsStream()
    }

    open fun getLocalTweets(): Flow<Result<List<Tweet>>> {
        return tweetRepository.getLocalTweetsStream().flowOn(ioDispatcher)
    }

    suspend fun refreshTweets() {
        tweetRepository.refreshTweets()
    }
}
