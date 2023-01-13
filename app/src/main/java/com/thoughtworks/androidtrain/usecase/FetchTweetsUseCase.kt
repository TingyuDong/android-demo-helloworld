package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

open class FetchTweetsUseCase(
    private val tweetsRepository: TweetsRepository,
    private val sendersRepository: SendersRepository,
    private val commentsRepository: CommentsRepository,
    private val imagesRepository: ImagesRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    open fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetsRepository.getRemoteTweetsStream()
    }

    open fun getLocalTweets(): Flow<Result<List<Tweet>>> {
        return combineFlow().flowOn(ioDispatcher)
    }

    suspend fun refreshTweets() {
        tweetsRepository.refreshTweets()
    }

    private fun combineFlow(): Flow<Result<List<Tweet>>> {
        return tweetsRepository.getLocalTweetsStream().map {
            Result.Success(transformToTweetList(it))
        }
    }

    private suspend fun transformToTweetList(tweetPOList: List<TweetPO>): List<Tweet> {
        return tweetPOList.map { tweetPO ->
            Tweet(
                id = tweetPO.id,
                content = tweetPO.content,
                sender = sendersRepository.getSender(tweetPO.senderName),
                images = imagesRepository.getImages(tweetPO.id),
                comments = commentsRepository.getComments(tweetPO.id),
                error = tweetPO.error,
                unknownError = tweetPO.unknownError
            )
        }
    }
}
