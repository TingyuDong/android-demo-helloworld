package com.thoughtworks.androidtrain

import android.app.Application
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.DatabaseRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import okhttp3.OkHttpClient

class TweetApplication : Application() {
    private lateinit var client: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initialize(this)
        client = OkHttpClient()
        addTweet()
    }

    fun getHttpClient() : OkHttpClient {
        return client
    }

    private fun addTweet() {
        val database: AppDatabase = DatabaseRepository.get().getDatabase()
        val senderDao = database.senderDao()
        val tweetDao = database.tweetDao()

        val tweetRepository = TweetRepository(tweetDao)
        val senderRepository = SenderRepository(senderDao)

        val sender = Sender(
            username = "Aiolia",
            nick = "Aio",
            avatar = "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        senderRepository.addSender(sender)
        tweetRepository.addTweet(
            Tweet(
                id = 7,
                content = "沙发",
                sender = sender,
                images = null,
                comments = null,
                error = null,
                unknownError = null
            )
        )
    }
}