package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.repository.ImagesRepository
import com.thoughtworks.androidtrain.data.repository.SendersRepository
import com.thoughtworks.androidtrain.data.repository.TweetsRepository
import com.thoughtworks.androidtrain.data.source.local.room.CommentWithSender
import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSenderAndCommentsAndImages
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

open class FetchTweetsUseCase(
    private val tweetsRepository: TweetsRepository,
    private val sendersRepository: SendersRepository,
    private val imagesRepository: ImagesRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    open fun fetchRemoteTweets(): Flow<Result<List<Tweet>>> {
        return tweetsRepository.getRemoteTweetsStream()
    }

    open fun getLocalTweets(): Flow<Result<List<Tweet>>> {
        return tweetsRepository.getLocalTweetsStream().map { tweetMapList ->
            Result.Success(transformToTweetList(tweetMapList))
        }.flowOn(ioDispatcher)
    }

    suspend fun refreshTweets() {
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
}
