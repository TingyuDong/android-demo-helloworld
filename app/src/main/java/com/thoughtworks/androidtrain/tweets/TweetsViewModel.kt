package com.thoughtworks.androidtrain.tweets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.launch

class TweetsViewModel(
    private val fetchTweetsUseCase: FetchTweetsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val addTweetUseCase: AddTweetUseCase
) : ViewModel() {

    private val _tweets = MutableLiveData(emptyList<Tweet>())
    val tweets: LiveData<List<Tweet>> = _tweets

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        viewModelScope.launch {
            fetchTweets()
        }
    }

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

    private suspend fun fetchTweets() {
        fetchTweetsUseCase.fetchLocalTweets().also { allLocalTweets ->
            fetchTweetsUseCase.fetchRemoteTweets()
                .onSuccess { _tweets.value = allLocalTweets.plus(it) }
                .onFailure {
                    _tweets.value = allLocalTweets
                    _message.setValue(it.message)
                }
        }
    }
}