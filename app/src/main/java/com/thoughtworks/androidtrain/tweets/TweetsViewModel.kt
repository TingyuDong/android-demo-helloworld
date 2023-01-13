package com.thoughtworks.androidtrain.tweets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success
import com.thoughtworks.androidtrain.data.Result.Error
import com.thoughtworks.androidtrain.data.Result.Loading
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class TweetsUiState(
    var tweets: List<Tweet> = emptyList(),
    val message: String? = null,
    val isRefreshing: Boolean = false
)

class TweetsViewModel(
    private val fetchTweetsUseCase: FetchTweetsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val addTweetUseCase: AddTweetUseCase
) : ViewModel() {
    private val _message = MutableStateFlow<String?>(null)

    private val _tweetsWithSenders = fetchTweetsUseCase.fetchTweetsWithSenders()

    private val _isRefreshing = MutableStateFlow(false)

    private var _tweets = combine(
        fetchTweetsUseCase.getLocalTweets(), fetchTweetsUseCase.fetchRemoteTweets()
    ) { tweetsLocalResult, tweetsRemoteResult ->
        handleResult(tweetsLocalResult, tweetsRemoteResult)
    }

    val uiState: StateFlow<TweetsUiState> = combine(
        _tweets, _message, _isRefreshing, _tweetsWithSenders
    ) { tweets, message, isRefreshing, tweetsWithSenders ->
        TweetsUiState(
            tweets = tweets,
            message = tweetsWithSenders.toString(),
            isRefreshing = isRefreshing
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = Lazily,
            initialValue = TweetsUiState(tweets = emptyList(), message = null, isRefreshing = true)
        )

    init {
        refresh()
    }

    fun saveComment(tweetId: Int, commentContent: String) {
        viewModelScope.launch {
            addCommentUseCase.invoke(tweetId, commentContent)
        }
    }

    fun saveTweet(tweet: Tweet) {
        viewModelScope.launch {
            addTweetUseCase.invoke(tweet)
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        viewModelScope.launch {
            fetchTweetsUseCase.refreshTweets()
            _isRefreshing.value = false
        }
    }

    fun cleanMessage() {
        _message.value = null
    }

    private fun handleResult(
        tweetsLocalResult: Result<List<Tweet>>,
        tweetsRemoteResult: Result<List<Tweet>>
    ): List<Tweet> {
        return when (tweetsRemoteResult) {
            Loading -> emptyList()
            is Success -> tweetsRemoteResult.data
            is Error -> {
                showErrorMessage(tweetsRemoteResult.toString())
                showLocalTweets(tweetsLocalResult)
            }
        }
    }

    private fun showLocalTweets(tweetsLocalResult: Result<List<Tweet>>): List<Tweet> {
        return when (tweetsLocalResult) {
            Loading -> emptyList()
            is Success -> tweetsLocalResult.data
            is Error -> {
                showErrorMessage(tweetsLocalResult.toString())
                return emptyList()
            }
        }
    }

    private fun showErrorMessage(message: String) {
        _message.value = message
    }
}
