package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import java.util.stream.Collectors
import kotlin.collections.ArrayList

interface TweetRepositoryInterface {
    fun fetchTweets(): List<Tweet>
    fun addTweet(tweet: Tweet)
    fun addAllTweet(tweets: ArrayList<Tweet>)
}

class TweetRepository : TweetRepositoryInterface {
    private val database: AppDatabase = DatabaseRepository.get().getDatabase()

    private val tweetDao = database.tweetDao()
    private val senderDao = database.senderDao()
    private val commentDao = database.commentDao()
    private val imageDao = database.imageDao()

    private val senderRepository = SenderRepository(senderDao)
    private val commentRepository = CommentRepository(commentDao, senderDao)
    private val imageRepository = ImageRepository(imageDao)

    override fun fetchTweets(): ArrayList<Tweet> {
        val tweets = tweetDao.getAll()
        val tweetData: List<Tweet> = tweets.filter { it.error==null&&it.unknownError==null }.stream().map {
            val sender = it.senderName?.let { it1 -> senderRepository.getSender(it1) }
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
        return ArrayList(tweetData)
    }

    override fun addTweet(tweet: Tweet) {
        tweet.sender?.let { senderRepository.addSender(it) }
        val tweetId = tweetDao.insertTweet(
            TweetPO(
                id = tweet.id,
                content = tweet.content,
                senderName = tweet.sender?.username,
                error = tweet.error,
                unknownError = tweet.unknownError
            )
        )
        tweet.comments?.let { commentRepository.addComments(it, tweetId.toInt()) }
        tweet.images?.let { imageRepository.addImages(it, tweetId.toInt()) }
    }

    override fun addAllTweet(tweets: ArrayList<Tweet>) {
        tweets.forEach { tweet -> addTweet(tweet) }
    }


}