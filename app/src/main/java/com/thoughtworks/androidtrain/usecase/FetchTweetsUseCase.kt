package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success
import com.thoughtworks.androidtrain.data.Result.Error
import com.thoughtworks.androidtrain.data.Result.Loading
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.repository.ImagesRepository
import com.thoughtworks.androidtrain.data.repository.SendersRepository
import com.thoughtworks.androidtrain.data.repository.TweetsRepository
import com.thoughtworks.androidtrain.data.source.local.room.CommentWithSender
import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSenderAndCommentsAndImages
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

open class FetchTweetsUseCase(
    private val tweetsRepository: TweetsRepository,
    private val sendersRepository: SendersRepository,
    private val imagesRepository: ImagesRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    private fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetsRepository.getRemoteTweetsStream()
    }

    private fun getLocalTweets(): Flow<List<Tweet>> {
        return tweetsRepository.getLocalTweetsStream().map { tweetMapList ->
            transformToTweetList(tweetMapList)
        }
            .flowOn(ioDispatcher)
    }

    open fun getTweets(): Flow<Result<List<Tweet>>> {
        return combine(
            fetchRemoteTweets(), getLocalTweets()
        ) { tweetsLocalResult, tweetsRemoteResult ->
            handleResult(tweetsRemoteResult, tweetsLocalResult)
        }
    }

    open suspend fun refreshTweets() {
        tweetsRepository.refreshTweets()
    }

    private fun transformToTweetList(
        tweetMapList: List<TweetWithSenderAndCommentsAndImages>
    ): List<Tweet> {
        return tweetMapList.map { tweetMap ->
            Tweet(
                id = tweetMap.tweetPO.id,
                content = tweetMap.tweetPO.content,
                sender = sendersRepository.transformToSender(tweetMap.senderPO),
                images = tweetMap.imagePOList?.let { imagesRepository.transformToImageList(it) },
                comments = tweetMap.commentPOList?.let {
                    transformToCommentList(it)
                },
                error = tweetMap.tweetPO.error,
                unknownError = tweetMap.tweetPO.unknownError
            )
        }
    }

    private fun transformToCommentList(
        commentsWithSenderList: List<CommentWithSender>
    ): List<Comment> {
        return commentsWithSenderList.mapNotNull { transformToComment(it) }
    }

    private fun transformToComment(commentWithSender: CommentWithSender): Comment? {
        return Comment(
            content = commentWithSender.commentPO.content,
            sender = sendersRepository.transformToSender(commentWithSender.senderPO) ?: return null
        )
    }

    private fun handleResult(
        tweetsLocalResult: List<Tweet>,
        tweetsRemoteResult: Result<List<Tweet>>
    ): Result<List<Tweet>> {
        return when (tweetsRemoteResult) {
            Loading -> Loading
            is Success -> Success(tweetsRemoteResult.data)
            is Error -> {
                Success(tweetsLocalResult)
            }
        }
    }
}
