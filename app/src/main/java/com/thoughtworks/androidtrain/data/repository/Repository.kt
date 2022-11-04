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
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
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

    override fun fetchTweets(): ArrayList<Tweet> {
        val tweets = tweetDao.getAll()
        val tweetData: List<Tweet> = tweets.stream().map {
            val sender = it.senderId?.let { it1 -> getSender(it1) }
            val images = getImages(it.tweetId)
            val comments = getComments(it.tweetId)
            Tweet(it.tweetId, it.content, sender, images, comments, it.error, it.unknownError)
        }.collect(Collectors.toList())
        return ArrayList(tweetData)
    }

    override fun addTweet(tweet: Tweet) {
        var senderId = addSender(tweet.sender)
        var tweetId = tweetDao.insertTweet(
            TweetPO(
                0,
                "hhh",
                senderId?.toInt(),
                tweet.error,
                tweet.unknownError
            )
        )
        addComments(tweet.comments, tweetId.toInt())
        addImages(tweet.images, tweetId.toInt())
    }

    override fun getSender(senderId: Int): Sender? {
        val senderPO = senderDao.getSender(senderId)
        if (senderPO != null) {
            return Sender(senderId, senderPO.userName, senderPO.nick, senderPO.avatar)
        }
        return null
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
                            it1.senderId,
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
            it.sender?.id?.let { it1 ->
                CommentPO(0, tweetId, it.content, it1)
//                commentDao.insertComments(commentPO)
            }
        }?.collect(Collectors.toList())
        if (commentsCollect != null) {
            commentDao.insertAllComments(commentsCollect)
        }

    }

    override fun addImages(images: List<Image>?, tweetId: Int) {
        val imagesCollect = images?.stream()?.map {
            it.id?.let { it1 -> ImagePO(it1, tweetId, it.url) }
        }?.collect(Collectors.toList())
        if (imagesCollect != null) {
            imageDao.insertAllImages(imagesCollect)
        }
    }

    override fun addSender(sender: Sender?): Long? {
        val senderPO = sender?.id?.let { SenderPO(it, sender.username, sender.nick, sender.avatar) }
        if (senderPO != null) {
            var insertSender = senderDao.insertSender(senderPO)
            return insertSender
        }
        return null
    }
}