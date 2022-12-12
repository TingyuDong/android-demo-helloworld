package com.thoughtworks.androidtrain.tweets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.Dispatchers
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

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            fetchTweetsUseCase.invoke()
                .onSuccess {
                    _tweets.setValue(it)
                }
                .onFailure {
                    _message.setValue(it.message)
                }

        }
    }

    fun saveComment(tweetId: Int, commentContent: String) {
        viewModelScope.launch(Dispatchers.Main) {
            addCommentUseCase.invoke(tweetId, commentContent)
            fetchData()
        }
    }

    fun saveTweet(tweet: Tweet) {
        viewModelScope.launch(Dispatchers.Main) {
            addTweetUseCase.invoke(tweet)
            fetchData()
        }
    }
}