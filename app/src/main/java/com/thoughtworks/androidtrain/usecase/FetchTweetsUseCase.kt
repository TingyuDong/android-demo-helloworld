package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.CommentsRepository
import com.thoughtworks.androidtrain.data.repository.ImagesRepository
import com.thoughtworks.androidtrain.data.repository.SendersRepository
import com.thoughtworks.androidtrain.data.repository.TweetsRepository
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

open class FetchTweetsUseCase(
    private val senderRepository: SendersRepository,
    private val commentRepository: CommentsRepository,
    private val imageRepository: ImagesRepository,
    private val tweetRepository: TweetsRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    open fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetRepository.getRemoteTweetsStream()
    }

    open fun getLocalTweets(): Flow<Result<List<Tweet>>> {
        return combine(
            tweetRepository.getLocalTweetsStream(),
            senderRepository.getSendersStream(),
            commentRepository.getCommentsStream(),
            imageRepository.getImagesStream()
        ) { tweetPOList, _, _, _ ->
            Success(transformToTweetList(tweetPOList))
        }
            .flowOn(ioDispatcher)
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
