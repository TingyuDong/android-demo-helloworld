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
        fetchTweetsUseCase.fetchLocalTweets(), fetchTweetsUseCase.fetchRemoteTweets()
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
        tweetRemoteResult: Result<List<Tweet>>
    ): List<Tweet> {
        _message.value = tweetRemoteResult.exceptionOrNull()?.message
        return tweetRemoteResult.getOrNull() ?: tweetsLocalResult.getOrNull() ?: emptyList()
    }
}
