package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.CommentRepository
import com.thoughtworks.androidtrain.data.repository.ImageRepository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.Flow

open class FetchTweetsUseCase(
    private val senderRepository: SenderRepository,
    private val commentRepository: CommentRepository,
    private val imageRepository: ImageRepository,
    private val tweetRepository: TweetRepository,
) {
    open fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetRepository.getRemoteTweetsStream()
    }

    open fun getLocalTweets(): Flow<Result<List<Tweet>>> {
        return combine(
            tweetRepository.getLocalTweetsStream(),
            senderRepository.getSendersStream(),
            commentRepository.getCommentsStream(),
            imageRepository.getCommentsStream()
        ) { tweetPOList, _, _, _ ->
            Result.Success(transformToTweetList(tweetPOList))
        }
    }

    private suspend fun transformToTweetList(tweetPOList: List<TweetPO>): List<Tweet> {
        return tweetPOList.map { tweetPO ->
            Tweet(
                id = tweetPO.id,
                content = tweetPO.content,
                sender = senderRepository.getSender(tweetPO.senderName),
                images = imageRepository.getImages(tweetPO.id),
                comments = commentRepository.getComments(tweetPO.id),
                error = tweetPO.error,
                unknownError = tweetPO.unknownError
            )
        }
    }

    suspend fun refreshTweets() {
        tweetRepository.refreshTweets()
    }
}
