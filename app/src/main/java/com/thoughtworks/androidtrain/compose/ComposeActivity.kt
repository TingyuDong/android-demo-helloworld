package com.thoughtworks.androidtrain.compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.R
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.utils.JSONResourceUtils

class ComposeActivity : AppCompatActivity() {
    var tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tweets.addAll(
            Gson().fromJson(
                JSONResourceUtils().jsonResourceReader(resources, R.raw.tweets),
                object : TypeToken<ArrayList<Tweet>>() {}.type
            )
        )
        setContent {
            TweetScreen(tweets)
        }
    }

    @Preview
    @Composable
    fun ContentPreview() {
        tweets.add(getTweet())
        tweets.add(getTweet())
        TweetScreen(tweets)
    }

    private fun getTweet(): Tweet {
        val sender = Sender(
            username = "Aiolia",
            nick = "Aio",
            avatar = "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        return Tweet(
            id = 7,
            content = "沙发",
            sender = sender,
            images = null,
            comments = null,
            error = null,
            unknownError = null
        )
    }
}