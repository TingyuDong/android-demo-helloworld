package com.thoughtworks.androidtrain.data.repository

import android.content.Context
import androidx.room.Room
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import java.util.stream.Collectors
import kotlin.collections.ArrayList

private const val DATABASE_NAME = "tweet"

class Repository private constructor(context: Context) : RepositoryInterface {
    private val database: AppDatabase =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()

    private val tweetDao = database.tweetDao()

    private val senderRepository = SenderRepository(this)
    private val commentRepository = CommentRepository(this)
    private val imageRepository = ImageRepository(this)

    companion object {
        private var INSTANCE: Repository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }
        }

        fun get(): Repository {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialized")
        }
    }

    fun getDatabase(): AppDatabase {
        return database
    }

    override fun fetchTweets(): ArrayList<Tweet> {
        val tweets = tweetDao.getAll()
        val tweetData: List<Tweet> = tweets.stream().map {
            val sender = it.senderId?.let { it1 -> senderRepository.getSender(it1) }
            val images = imageRepository.getImages(it.id)
            val comments = commentRepository.getComments(it.id)
            Tweet(it.id, it.content, sender, images, comments, it.error, it.unknownError)
        }.collect(Collectors.toList())
        return ArrayList(tweetData)
    }

    override fun addTweet(tweet: Tweet) {
        val senderId = senderRepository.addSender(tweet.sender)
        val tweetId = tweetDao.insertTweet(
            TweetPO(
                tweet.id,
                tweet.content,
                senderId?.toInt(),
                tweet.error,
                tweet.unknownError
            )
        )
        commentRepository.addComments(tweet.comments, tweetId.toInt())
        imageRepository.addImages(tweet.images, tweetId.toInt())
    }

    override fun addAllTweet(tweets: ArrayList<Tweet>) {
        tweets.forEach { tweet -> addTweet(tweet) }
    }


}