package com.thoughtworks.androidtrain

import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import kotlin.collections.ArrayList

class TweetsViewModel : ViewModel() {
    private lateinit var okHttpClient: OkHttpClient

    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase
    private lateinit var addCommentUseCase: AddCommentUseCase
    private lateinit var addTweetUseCase: AddTweetUseCase

    var localTweetsData = MutableLiveData(ArrayList<Tweet>())
    var remoteTweetsData = MutableLiveData(ArrayList<Tweet>())
    val tweets: LiveData<ArrayList<Tweet>>
        get() = remoteTweetsData

    fun init(okHttpClient: OkHttpClient) {
        this.okHttpClient = okHttpClient
        this.fetchTweetsUseCase = FetchTweetsUseCase(okHttpClient)
        this.addCommentUseCase = AddCommentUseCase()
        this.addTweetUseCase = AddTweetUseCase(okHttpClient)
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                remoteTweetsData.postValue(fetchTweetsUseCase.invoke())
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
        viewModelScope.launch (Dispatchers.Main) {
            addCommentUseCase.invoke(comment, tweetId)
        }
    }

    fun saveTweet(tweet: Tweet) {
        viewModelScope.launch (Dispatchers.Main) {
            addTweetUseCase.invoke(tweet)
        }
    }

//    fun getUserInfo(): Sender{
//        getApplication<>()
//        val settings: SharedPreferences = getApplication().getSharedPreferences("UserInfo", 0);
//        return Sender()
//    }
}