package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.CommentRepository
import com.thoughtworks.androidtrain.data.repository.ImageRepository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class FetchTweetsUseCase(
    private val senderRepository: SenderRepository,
    private val commentRepository: CommentRepository,
    private val imageRepository: ImageRepository,
    private val tweetRepository: TweetRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    open fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetRepository.getRemoteTweetsStream()
    }

    suspend fun refreshTweets(){
        tweetRepository.refreshTweets()
    }

    suspend fun fetchLocalTweets(): List<Tweet> = withContext(ioDispatcher) {
        val allLocalTweets: List<Tweet> =
            tweetRepository.getAllLocalTweets()
                .filter { it.error == null && it.unknownError == null }.map {
                    val sender =
                        it.senderName?.let { senderName -> senderRepository.getSender(senderName) }
                    val images = imageRepository.getImages(it.id)
                    val comments = commentRepository.getComments(it.id)
                    Tweet(
                        id = it.id,
                        content = it.content,
                        sender = sender,
                        images = images,
                        comments = comments,
                        error = it.error,
                        unknownError = it.unknownError
                    )
                }
        return@withContext allLocalTweets
    }
}
