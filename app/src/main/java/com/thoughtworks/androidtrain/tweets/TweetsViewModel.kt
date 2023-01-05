package com.thoughtworks.androidtrain.tweets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success

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

    private val _isRefreshing = MutableStateFlow(false)

    private var _tweets = combine(
        fetchTweetsUseCase.getLocalTweets(), fetchTweetsUseCase.fetchRemoteTweets()
    ) { tweetsLocalResult, tweetsRemoteResult ->
        handleResult(tweetsLocalResult, tweetsRemoteResult)
    }.flowOn(Dispatchers.IO)

    val uiState: StateFlow<TweetsUiState> = combine(
        _tweets, _message, _isRefreshing
    ) { tweets, message, isRefreshing ->
        TweetsUiState(
            tweets = tweets,
            message = message,
            isRefreshing = isRefreshing
        )
    }.stateIn(
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
        return if (tweetsRemoteResult is Success) tweetsRemoteResult.data
        else if (tweetsLocalResult is Success) {
            showErrorMessage(tweetsRemoteResult.toString())
            tweetsLocalResult.data
        } else {
            showErrorMessage(FAILED_TO_GET_THE_DATA)
            emptyList()
        }
    }

    private fun showErrorMessage(message: String) {
        _message.value = message
    }


}

private const val FAILED_TO_GET_THE_DATA = "Failed to get the data"
