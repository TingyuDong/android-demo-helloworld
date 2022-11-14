package com.thoughtworks.androidtrain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.CommentRepository
import com.thoughtworks.androidtrain.data.repository.DatabaseRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import kotlin.collections.ArrayList

class TweetsViewModel : ViewModel() {
    private lateinit var okHttpClient: OkHttpClient

    private val database: AppDatabase = DatabaseRepository.get().getDatabase()
    private val senderDao = database.senderDao()
    private val commentDao = database.commentDao()

    private val tweetRepository = TweetRepository()
    private val commentRepository = CommentRepository(commentDao, senderDao)

    var localTweetsData = MutableLiveData(ArrayList<Tweet>())
    var remoteTweetsData = MutableLiveData(ArrayList<Tweet>())
    val tweets: LiveData<ArrayList<Tweet>>
        get() = remoteTweetsData

    fun init(okHttpClient: OkHttpClient) {
        this.okHttpClient = okHttpClient
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                remoteTweetsData.postValue(tweetRepository.fetchTweets())
                localTweetsData.postValue(fetchTweetFromNetwork())
            }
        }
    }

    private fun fetchTweetFromNetwork(): ArrayList<Tweet>? {
        val url = "https://thoughtworks-mobile-2018.herokuapp.com/user/jsmith/tweets"
        val response = okHttpClient.newCall(
            Request.Builder()
                .url(url)
                .build()
        ).execute()
        return Gson().fromJson(
            Objects.requireNonNull(response.body?.string()),
            object : TypeToken<ArrayList<Tweet>>() {}.type
        )
    }

    fun saveComment(comment: Comment, tweetId: Int) {
        commentRepository.addComment(comment, tweetId)
    }
}