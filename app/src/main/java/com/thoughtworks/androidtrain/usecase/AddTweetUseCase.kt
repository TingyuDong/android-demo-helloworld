package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTweetUseCase(
    private val senderRepository: SenderRepository,
    private val commentRepository: CommentRepository,
    private val imageRepository: ImageRepository,
    private val tweetRepository: TweetRepository
) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    suspend operator fun invoke(tweet: Tweet) = withContext(defaultDispatcher) {
        tweet.sender?.also { senderRepository.addSender(it) }
        val tweetId = tweetRepository.addTweet(
            TweetPO(
                id = tweet.id,
                content = tweet.content,
                senderName = tweet.sender?.username,
                error = tweet.error,
                unknownError = tweet.unknownError
            )
        )
        tweet.comments?.also { commentRepository.addComments(it, tweetId.toInt()) }
        tweet.images?.also { imageRepository.addImages(it, tweetId.toInt()) }
    }
}