package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import kotlinx.coroutines.*
import okhttp3.Request
import java.util.Objects

class TweetsActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var tweetsAdapter: TweetsAdapter
    private val tweetRepository = TweetRepository()
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
            Toast.makeText(
                this,
                "数据改变$it",
                Toast.LENGTH_SHORT
            ).show()
            createTweets()
            tweetsAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private fun initUI() {
        recyclerView = findViewById(R.id.tweet_recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tweetsAdapter = TweetsAdapter(tweets)
        recyclerView.adapter = tweetsAdapter
        createTweets()
        tweetsAdapter.setTweet(tweets)
    }

    private fun createTweets(){
//        val adapterType = object : TypeToken<ArrayList<Tweet?>>() {}.type
//        val repositoryType = object : TypeToken<ArrayList<Tweet>>() {}.type
//        val jsonString = JSONResourceUtils().jsonResourceReader(resources, R.raw.tweets)
//        tweets.addAll(Gson().fromJson<ArrayList<Tweet?>?>(jsonString, adapterType))
//        tweetRepository.addAllTweet(Gson().fromJson(jsonString, repositoryType))
        tweets.clear()

        tweets.addAll(tweetRepository.fetchTweets())
//        getTweetFromNet()
        addEmptyData(tweets)
    }

//    private fun getTweetFromNet() {
//        val tweetsActivity = this
//        launch(Dispatchers.Main) {
//            withContext(Dispatchers.IO) {
//                val url = "https://thoughtworks-mobile-2018.herokuapp.com/user/jsmith/tweets"
//                val request = Request.Builder()
//                    .url(url)
//                    .build()
//                val response =
//                    (application as TweetApplication).getHttpClient().newCall(request).execute()
//                val obj = Objects.requireNonNull(response.body?.string())
////                var result = response.body.toString()
//                val type = object : TypeToken<ArrayList<Tweet>>() {}.type
//                val tweetsFromNetwork = Gson().fromJson<ArrayList<Tweet>?>(obj, type)
//                tweetRepository.addAllTweet(tweetsFromNetwork)
//            }
//            Toast.makeText(tweetsActivity, "加载完毕", Toast.LENGTH_SHORT).show()
//        }
//        Toast.makeText(tweetsActivity, "加载中", Toast.LENGTH_SHORT).show()
//    }

    private fun addEmptyData(tweets: ArrayList<Tweet?>) {
        tweets.add(null)
    }
}