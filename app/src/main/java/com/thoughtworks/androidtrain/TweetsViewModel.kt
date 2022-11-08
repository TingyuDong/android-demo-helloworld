package com.thoughtworks.androidtrain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

class TweetsViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var okHttpClient: OkHttpClient
    private val tweetRepository = TweetRepository()
    var tweetsDataFromNetwork = MutableLiveData(ArrayList<Tweet>())
    var tweetsDataFromDB = MutableLiveData(ArrayList<Tweet>())

    fun init(okHttpClient: OkHttpClient) {
        this.okHttpClient = okHttpClient
    }

    fun fetchData() {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                tweetsDataFromDB.postValue(tweetRepository.fetchTweets())
                fetchTweetFromNetwork()
            }
        }
    }

    private fun fetchTweetFromNetwork() {
        val url = "https://thoughtworks-mobile-2018.herokuapp.com/user/jsmith/tweets"
        val request = Request.Builder()
            .url(url)
            .build()
        val response = okHttpClient.newCall(request).execute()
        val obj = Objects.requireNonNull(response.body?.string())
        //                var result = response.body.toString()
        val type = object : TypeToken<ArrayList<Tweet>>() {}.type
        val tweetsFromNetwork = Gson().fromJson<ArrayList<Tweet>?>(obj, type)
        tweetsDataFromNetwork.postValue(tweetsFromNetwork)
        //                tweetRepository.addAllTweet(tweetsFromNetwork)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}