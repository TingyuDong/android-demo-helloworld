package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class AddTweetUseCase(
    private val senderRepository: SendersRepositoryImpl,
    private val commentRepository: CommentsRepositoryImpl,
    private val imageRepository: ImagesRepositoryImpl,
    private val tweetRepository: TweetsRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(tweet: Tweet) = withContext(ioDispatcher) {
        tweet.sender?.also { senderRepository.addSender(it) }
        val tweetId = tweetRepository.addTweet(
            TweetPO(
                id = tweet.id,
                content = tweet.content,
                senderName = tweet.sender?.username ?: return@withContext ,
                error = tweet.error,
                unknownError = tweet.unknownError
            )
        )
        tweet.comments?.also { commentRepository.addComments(it, tweetId.toInt()) }
        tweet.images?.also { imageRepository.addImages(it, tweetId.toInt()) }
    }
}