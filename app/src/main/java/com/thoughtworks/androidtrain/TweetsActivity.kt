package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TweetsActivity : AppCompatActivity() {
    private lateinit var tweetsAdapter: TweetsAdapter
    private val tweets = ArrayList<Tweet?>()
    private val tweetViewModel: TweetsViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        initViewModel()
        initUI()
    }

    private fun initViewModel() {
        tweetViewModel.tweets.observe(this) {
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