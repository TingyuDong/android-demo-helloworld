package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.source.local.room.TweetsLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface TweetsRepositoryInterface {
    fun getAllLocalTweets(): List<TweetPO>
    fun getRemoteTweetsStream(): Flow<Result<List<Tweet>>>
    fun getLocalTweetsStream(): Flow<Result<List<Tweet>>>
    suspend fun refreshTweets()
    fun addTweet(tweetPO: TweetPO): Long
    suspend fun transformToTweetList(tweetPOList: List<TweetPO>): List<Tweet>
}

class TweetsRepository(
    private val tweetsRemoteDataSource: TweetsRemoteDataSource,
    private val tweetsLocalDataSource: TweetsLocalDataSource,
    private val senderRepository: SendersRepository,
    private val commentRepository: CommentsRepository,
    private val imageRepository: ImagesRepository,
) : TweetsRepositoryInterface {
    override fun getAllLocalTweets(): List<TweetPO> {
        return tweetsLocalDataSource.getTweets()
    }

    override fun getRemoteTweetsStream(): Flow<Result<List<Tweet>>> {
        return tweetsRemoteDataSource.getTweetsStream()
    }

    override fun getLocalTweetsStream(): Flow<Result<List<Tweet>>> {
        return combine(
            tweetsLocalDataSource.getTweetsStream(),
            senderRepository.getSendersStream(),
            commentRepository.getCommentsStream(),
            imageRepository.getImagesStream()
        ) { tweetPOList, _, _, _ ->
            Result.Success(transformToTweetList(tweetPOList))
        }
    }

    override suspend fun refreshTweets() {
        tweetsRemoteDataSource.refreshTweets()
    }

    override fun addTweet(tweetPO: TweetPO): Long {
        return tweetsLocalDataSource.addTweet(tweetPO)
    }

    override suspend fun transformToTweetList(tweetPOList: List<TweetPO>): List<Tweet> {
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
}