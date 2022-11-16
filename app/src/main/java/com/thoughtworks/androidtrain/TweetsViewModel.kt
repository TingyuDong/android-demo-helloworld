package com.thoughtworks.androidtrain

import androidx.lifecycle.*
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import kotlin.collections.ArrayList

class TweetsViewModel : ViewModel() {
    private lateinit var okHttpClient: OkHttpClient

    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase
    private lateinit var addCommentUseCase: AddCommentUseCase
    private lateinit var addTweetUseCase: AddTweetUseCase

    var tweetsData = MutableLiveData(ArrayList<Tweet>())
    val tweets: LiveData<ArrayList<Tweet>>
        get() = tweetsData

    fun init(okHttpClient: OkHttpClient) {
        this.okHttpClient = okHttpClient
        this.fetchTweetsUseCase = FetchTweetsUseCase(okHttpClient)
        this.addCommentUseCase = AddCommentUseCase()
        this.addTweetUseCase = AddTweetUseCase(okHttpClient)
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                tweetsData.postValue(fetchTweetsUseCase.invoke())
            }
        }
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