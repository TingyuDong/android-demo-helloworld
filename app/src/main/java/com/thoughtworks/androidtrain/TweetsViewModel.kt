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
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class TweetsViewModel(
    private var fetchTweetsUseCase: FetchTweetsUseCase,
    private var addCommentUseCase: AddCommentUseCase,
    private var addTweetUseCase: AddTweetUseCase,
    private var application: Application
) : ViewModel() {
    var tweetsData = MutableLiveData(ArrayList<Tweet>())
    val tweets: LiveData<ArrayList<Tweet>>
        get() = tweetsData

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                tweetsData.postValue(fetchTweetsUseCase.invoke())
            }
        }
    }

    fun saveComment(comment: Comment, tweetId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            addCommentUseCase.invoke(comment, tweetId)
        }
    }

    fun saveTweet(tweet: Tweet) {
        viewModelScope.launch(Dispatchers.Main) {
            addTweetUseCase.invoke(tweet)
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