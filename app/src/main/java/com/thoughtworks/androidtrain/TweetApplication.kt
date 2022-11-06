package com.thoughtworks.androidtrain

import android.app.Application
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.DatabaseRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase

class TweetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initialize(this)
        addTweet()
    }

    private fun addTweet() {
        val database: AppDatabase = DatabaseRepository.get().getDatabase()
        val senderDao = database.senderDao()

        val tweetRepository = TweetRepository()
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