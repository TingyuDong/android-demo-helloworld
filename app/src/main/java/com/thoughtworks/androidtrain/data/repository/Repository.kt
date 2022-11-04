package com.thoughtworks.androidtrain.data.repository

import android.content.Context
import androidx.room.Room
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import java.util.stream.Collectors
import kotlin.collections.ArrayList

private const val DATABASE_NAME = "tweet"

class Repository private constructor(context: Context) : TweetRepository {
    private val database: AppDatabase =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()

    private val tweetDao = database.tweetDao()
    private val senderDao = database.senderDao()
    private val imageDao = database.imageDao()
    private val commentDao = database.commentDao()

    private val senderRepository = SenderRepository(this)

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
            val images = getImages(it.id)
            val comments = getComments(it.id)
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
        addComments(tweet.comments, tweetId.toInt())
        addImages(tweet.images, tweetId.toInt())
    }

    override fun addAllTweet(tweets: ArrayList<Tweet>) {
        tweets.forEach { tweet -> addTweet(tweet) }
    }

    override fun getImages(tweetId: Int): List<Image>? {
        val imagesPO = imageDao.getImages(tweetId)
        if (imagesPO != null) {
            return imagesPO.stream().map { Image(it.id, it.url) }.collect(Collectors.toList())
        }
        return null
    }

    override fun getComments(tweetId: Int): List<Comment>? {
        val commentsPO = commentDao.getComments(tweetId)
        if (commentsPO != null) {
            return commentsPO.stream().map {
                val sender = senderDao.getSender(it.senderId)
                Comment(it.content,
                    sender?.let { it1 ->
                        Sender(
                            it1.id,
                            it1.userName,
                            sender.nick,
                            sender.avatar
                        )
                    })
            }.collect(Collectors.toList())
        }
        return null
    }

    override fun addComments(comments: List<Comment>?, tweetId: Int) {
        val commentsCollect = comments?.stream()?.map {
            val senderId = senderRepository.addSender(it.sender)
            senderId?.let { it1 -> CommentPO(0, tweetId, it.content, it1.toInt()) }
        }?.collect(Collectors.toList())
        if (commentsCollect != null) {
            commentDao.insertAllComments(commentsCollect)
        }
    }

    override fun addImages(images: List<Image>?, tweetId: Int) {
        val imagesCollect = images?.stream()?.map {
            if (it.id == null) {
                ImagePO(0, tweetId, it.url)
            } else {
                ImagePO(it.id, tweetId, it.url)
            }
        }?.collect(Collectors.toList())
        if (imagesCollect != null) {
            imageDao.insertAllImages(imagesCollect)
        }
    }
}