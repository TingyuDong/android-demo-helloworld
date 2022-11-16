package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

class FetchTweetsUseCase {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    private val database: AppDatabase = DatabaseRepository.get().getDatabase()
    private val tweetDao = database.tweetDao()
    private val senderDao = database.senderDao()
    private val commentDao = database.commentDao()
    private val imageDao = database.imageDao()

    private val senderRepository = SenderRepository(senderDao)
    private val commentRepository = CommentRepository(commentDao, senderDao)
    private val imageRepository = ImageRepository(imageDao)
    private val tweetRepository = TweetRepository(tweetDao)

    suspend operator fun invoke(): ArrayList<Tweet> = withContext(defaultDispatcher) {
        val tweets = tweetRepository.getAllTweets()
        val tweetData: List<Tweet> =
            tweets.filter { it.error == null && it.unknownError == null }.stream().map {
                val sender = it.senderName?.let { senderName -> senderRepository.getSender(senderName) }
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
            }.collect(Collectors.toList())
        ArrayList(tweetData)
    }
}