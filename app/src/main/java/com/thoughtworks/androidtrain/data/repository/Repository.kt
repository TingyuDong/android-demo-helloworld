package com.thoughtworks.androidtrain.data.repository

import android.content.Context
import androidx.room.Room
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import java.util.stream.Collectors

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
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

    override fun fetchTweets(): List<Tweet> {
        val tweets = tweetDao.getAll()
        val tweetData: List<Tweet> = tweets.stream().map {
            val sender = it.senderId?.let { it1 -> getSender(it1) }
            val images = getImages(it.tweetId)
            val comments = getComments(it.tweetId)
            Tweet(it.tweetId, it.content, sender, images, comments, it.error, it.unknownError)
        }.collect(Collectors.toList())
        return tweetData
    }

    override fun getSender(id: Int): Sender? {
        val sender = senderDao.getSender(id)
        if (sender != null) {
            return Sender(sender.userName, sender.nick, sender.avatar)
        }
        return null
    }

    override fun getImages(tweetId: Int): List<Image>? {
        val Images = imageDao.getImages(tweetId)
        if (Images != null) {
            return Images.stream().map { Image(it.url) }.collect(Collectors.toList())
        }
        return null
    }

    override fun getComments(tweetId: Int): List<Comment>? {
        val comments = commentDao.getComments(tweetId)
        if (comments != null){
            return comments.stream().map {
                val sender = senderDao.getSender(it.senderId)
                Comment(it.content,sender)
            }.collect(Collectors.toList())
        }
        return null
    }
}