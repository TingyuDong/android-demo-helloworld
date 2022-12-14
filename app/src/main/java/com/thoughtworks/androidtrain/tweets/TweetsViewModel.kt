package com.thoughtworks.androidtrain.tweets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    val uiState: StateFlow<TweetsUiState> = combine(
        fetchTweetsUseCase.fetchRemoteTweets(), _message, _isRefreshing
    ) { tasksResult, _, isRefreshing ->
        TweetsUiState(
            tweets = tasksResult.getOrNull() ?: emptyList(),
            message = tasksResult.exceptionOrNull()?.message,
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

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(3000)
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