package com.thoughtworks.androidtrain

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TweetsViewModel(
    private val fetchTweetsUseCase: FetchTweetsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val addTweetUseCase: AddTweetUseCase,
    private val application: Application
) : ViewModel() {
    private val tweetsData = MutableLiveData(listOf<Tweet>())
    val tweets: LiveData<List<Tweet>>
        get() = tweetsData

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            tweetsData.postValue(fetchTweetsUseCase.invoke())
        }
    }

    fun saveComment(comment: Comment, tweetId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            addCommentUseCase.invoke(comment, tweetId)
            tweetsData.postValue(fetchTweetsUseCase.invoke())
        }
    }

    fun saveTweet(tweet: Tweet) {
        viewModelScope.launch(Dispatchers.Main) {
            addTweetUseCase.invoke(tweet)
            tweetsData.postValue(fetchTweetsUseCase.invoke())
        }
    }

    fun getUserInfo(): Sender {
        val settings: SharedPreferences = application.getSharedPreferences("UserInfo", 0)
        val userName = settings.getString("UserName", "you")
        val nick = settings.getString("Nick", "you")
        val avatar = settings.getString("Avatar", "Avatar.png")
        return Sender(
            username = userName!!,
            nick = nick!!,
            avatar = avatar!!
        )
    }
}