package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.data.model.Tweet

class TweetsActivity : AppCompatActivity() {
    private lateinit var tweetsAdapter: TweetsAdapter
    private val tweets = ArrayList<Tweet?>()
    private lateinit var tweetViewModel: TweetsViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        initViewModel()
        initUI()
        tweetViewModel.fetchData()
    }

    private fun initViewModel() {
        tweetViewModel = ViewModelProvider(this)[TweetsViewModel::class.java]
        tweetViewModel.init((application as TweetApplication).getHttpClient())
        tweetViewModel.tweetsData.observe(this) {
            tweets.addAll(it)
            addEmptyData()
            tweetsAdapter.notifyDataSetChanged()
        }
    }

    private fun initUI() {
        recyclerView = findViewById(R.id.tweet_recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tweetsAdapter = TweetsAdapter(tweets)
        recyclerView.adapter = tweetsAdapter
    }

    private fun addEmptyData() {
        tweets.add(null)
    }
}