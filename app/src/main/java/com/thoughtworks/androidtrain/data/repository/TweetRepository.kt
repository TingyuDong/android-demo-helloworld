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
    private val databaseRepository = DatabaseRepository.get()

    private val database: AppDatabase = databaseRepository.getDatabase()

    private val tweetDao = database.tweetDao()

    private val senderRepository = SenderRepository()
    private val commentRepository = CommentRepository()
    private val imageRepository = ImageRepository()

    override fun fetchTweets(): ArrayList<Tweet> {
        val tweets = tweetDao.getAll()
        val tweetData: List<Tweet> = tweets.stream().map {
            val sender = it.senderName?.let { it1 -> senderRepository.getSender(it1) }
            val images = imageRepository.getImages(it.id)
            val comments = commentRepository.getComments(it.id)
            Tweet(it.id, it.content, sender, images, comments, it.error, it.unknownError)
        }.collect(Collectors.toList())
        return ArrayList(tweetData)
    }

    override fun addTweet(tweet: Tweet) {
        tweet.sender?.let { senderRepository.addSender(it) }
        val tweetId = tweetDao.insertTweet(
            TweetPO(
                tweet.id,
                tweet.content,
                senderName = tweet.sender?.username,
                tweet.error,
                tweet.unknownError
            )
        )
        tweet.comments?.let { commentRepository.addComments(it, tweetId.toInt()) }
        tweet.images?.let { imageRepository.addImages(it, tweetId.toInt()) }
    }

    override fun addAllTweet(tweets: ArrayList<Tweet>) {
        tweets.forEach { tweet -> addTweet(tweet) }
    }


}