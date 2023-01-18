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
import com.thoughtworks.androidtrain.utils.Async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.onStart

data class TweetsUiState(
    var tweets: List<Tweet> = emptyList(),
    val message: String? = null,
    val isRefreshing: Boolean = false
)

private const val ERROR_WHILE_LOADING_TASKS = "Error while loading tasks"

class TweetsViewModel(
    private val fetchTweetsUseCase: FetchTweetsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val addTweetUseCase: AddTweetUseCase
) : ViewModel() {
    private val _message = MutableStateFlow<String?>(null)

    private val _isRefreshing = MutableStateFlow(false)

    private var _tweetsAsync = fetchTweetsUseCase.invoke()
        .map { handleResult(it) }
        .onStart { emit(Async.Loading) }

    val uiState: StateFlow<TweetsUiState> = combine(
        _tweetsAsync, _message, _isRefreshing
    ) { tweetsAsync, message, isRefreshing ->
        when (tweetsAsync) {
            Async.Loading -> {
                TweetsUiState(isRefreshing = true)
            }
            is Async.Success -> {
                TweetsUiState(
                    tweets = tweetsAsync.data,
                    message = message,
                    isRefreshing = isRefreshing
                )
            }
        }
    }
        .stateIn(
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
        tweetsResult: Result<List<Tweet>>
    ): Async<List<Tweet>> {
        return when (tweetsResult) {
            Loading -> Async.Loading
            is Success -> Async.Success(tweetsResult.data)
            is Error -> {
                showErrorMessage(ERROR_WHILE_LOADING_TASKS)
                Async.Success(emptyList())
            }
        }
    }

    private fun showErrorMessage(message: String) {
        _message.value = message
    }
}
