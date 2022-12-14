package com.thoughtworks.androidtrain.tweets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.delay
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

    private val _tweets = fetchTweetsUseCase.fetchRemoteTweets()
        .map { handleResult(it) }

    val uiState: StateFlow<TweetsUiState> = combine(
        _tweets, _message, _isRefreshing
    ) { tweets, message, isRefreshing ->
        TweetsUiState(
            tweets = tweets,
            message = message,
            isRefreshing = isRefreshing
        )
            .apply {
                this.tweets = fetchTweetsUseCase.fetchLocalTweets() + this.tweets
            }
    }.stateIn(
        scope = viewModelScope,
        started = Lazily,
        initialValue = TweetsUiState(tweets = emptyList(), message = null, isRefreshing = true)
    )

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

    private fun fetchTweets() {
        TODO("Not yet implemented")
    }

    fun refresh() {
        _isRefreshing.value = true
        viewModelScope.launch {
            delay(5000)
            _isRefreshing.value = false
        }
    }

    fun cleanMessage() {
        _message.value = null
    }

    private fun handleResult(tweetResult: Result<List<Tweet>>): List<Tweet> {
        _message.value = tweetResult.exceptionOrNull()?.message
        return tweetResult.getOrNull() ?: emptyList()
    }
}
