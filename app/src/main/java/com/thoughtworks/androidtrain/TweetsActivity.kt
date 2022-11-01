package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.model.Tweet
import com.thoughtworks.androidtrain.utils.JSONResourceUtils

class TweetsActivity : AppCompatActivity() {
    private lateinit var tweetsAdapter: TweetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        initUI()
    }

    private fun initUI() {
        val recyclerView: RecyclerView = findViewById(R.id.tweet_recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tweetsAdapter = TweetsAdapter()
        recyclerView.adapter = tweetsAdapter
        createTweets()?.let { tweetsAdapter.setTweet(it) }
    }

    private fun createTweets(): ArrayList<Tweet>? {
        val type = object : TypeToken<ArrayList<Tweet>>() {}.type
        val jsonString = JSONResourceUtils().jsonResourceReader(resources, R.raw.tweets)
        val tweets = Gson().fromJson<ArrayList<Tweet>?>(jsonString, type)
        addEmptyData(tweets)
        return tweets
    }

    private fun addEmptyData(tweets: ArrayList<Tweet>) {
        val tweet = Tweet(
            tweets.size + 1,
            null, null, null, null, null, null
        )
        tweets.add(tweet)
    }
}