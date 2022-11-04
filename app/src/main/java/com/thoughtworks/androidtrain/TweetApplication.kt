package com.thoughtworks.androidtrain

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.Repository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TweetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
        val repository = Repository.get()
        addTweet(repository)
    }

    private fun addTweet(repository: Repository) {
        val senderRepository = SenderRepository(repository)
        val sender = Sender(
            8,
            "Aiolia",
            "Aio",
            "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        val senderId = senderRepository.addSender(
            sender
        )
        if (senderId != null) {
            sender.setId(senderId.toInt())
        }
        repository.addTweet(
            Tweet(
                7,
                "沙发",
                sender,
                null,
                null,
                null,
                null
            )
        )
        Log.i("TweetApplication:", senderId.toString())
    }
}