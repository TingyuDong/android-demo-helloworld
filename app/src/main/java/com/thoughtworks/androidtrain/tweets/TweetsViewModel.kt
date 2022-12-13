package com.thoughtworks.androidtrain.tweets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TweetsViewModel(
    private val fetchTweetsUseCase: FetchTweetsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val addTweetUseCase: AddTweetUseCase
) : ViewModel() {
    private val _tweets = MutableStateFlow(emptyList<Tweet>())
    val tweets: StateFlow<List<Tweet>> = _tweets

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing



//    init {
//        viewModelScope.launch {
//            fetchTweets()
//        }
//    }

    fun saveComment(tweetId: Int, commentContent: String) {
        viewModelScope.launch {
            addCommentUseCase.invoke(tweetId, commentContent)
            fetchTweets()
        }
    }

    fun saveTweet(tweet: Tweet) {
        viewModelScope.launch {
            addTweetUseCase.invoke(tweet)
            fetchTweets()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetchTweets()
            _isRefreshing.value = false
        }
    }

    fun cleanMessage() {
        _message.value = ""
    }

    private suspend fun fetchTweets() {
//        fetchTweetsUseCase.fetchRemoteTweets()
    }
}