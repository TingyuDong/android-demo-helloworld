package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.model.Tweet

class TweetsActivity : AppCompatActivity() {
    private lateinit var tweetsAdapter: TweetsAdapter

    object JsonData {
        const val data = "[" +
                "{\"content\":\"我上幼儿园了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上小学了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}," +
                "{\"content\":\"我上初中了\"," +
                "\"sender\":{\"username\":\"Aiolos\",\"nick\":\"oo\",\"avatar\":\"avatar.png\"}," +
                "\"images\":null,\"comments\":null,\"error\":\"null\",\"unknownError\":null}" +
                "]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        getJson()
    }

    private fun getJson() {
        val recyclerView: RecyclerView = findViewById(R.id.tweet_recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tweetsAdapter = TweetsAdapter()
        recyclerView.adapter = tweetsAdapter
        createTweets()?.let { tweetsAdapter.setTweet(it) }
        Toast.makeText(this, tweetsAdapter.tweets.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun createTweets(): ArrayList<Tweet>? {
        val type = object : TypeToken<ArrayList<Tweet>>() {}.type
        return Gson().fromJson(JsonData.data, type)
    }
}