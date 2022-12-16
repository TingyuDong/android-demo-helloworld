package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.CommentRepository
import com.thoughtworks.androidtrain.data.repository.ImageRepository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class FetchTweetsUseCase(
    private val senderRepository: SenderRepository,
    private val commentRepository: CommentRepository,
    private val imageRepository: ImageRepository,
    private val tweetRepository: TweetRepository,
) {
    open fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetRepository.getRemoteTweetsStream()
    }

    open fun fetchLocalTweets(): Flow<Result<List<Tweet>>> {
        return tweetRepository.getLocalTweetsStream()
            .map { tweetPOResult ->
                tweetPOResult.map { tweetPOList ->
                    tweetPOList.map { tweetPO ->
                        val sender =
                            tweetPO.senderName?.let { senderName ->
                                senderRepository.getSender(
                                    senderName
                                )
                            }
                        val images = imageRepository.getImages(tweetPO.id)
                        val comments = commentRepository.getComments(tweetPO.id)
                        Tweet(
                            id = tweetPO.id,
                            content = tweetPO.content,
                            sender = sender,
                            images = images,
                            comments = comments,
                            error = tweetPO.error,
                            unknownError = tweetPO.unknownError
                        )
                    }
                }
            }
    }

    suspend fun refreshTweets() {
        tweetRepository.refreshTweets()
    }
}
