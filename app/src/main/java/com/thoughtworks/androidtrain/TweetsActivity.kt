package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.Repository
import com.thoughtworks.androidtrain.utils.JSONResourceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TweetsActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var tweetsAdapter: TweetsAdapter
    private val repository = Repository.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        initUI()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private fun initData() {
//        launch {
//            val all = repository.fetchTweets()
//        }
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
        repository.addAllTweet(tweets)
        tweets.addAll(repository.fetchTweets())
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