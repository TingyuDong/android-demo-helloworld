package com.thoughtworks.androidtrain

import androidx.lifecycle.*
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class TestViewModel(
    private val fetchTweetsUseCase: FetchTweetsUseCase,
) : ViewModel() {

    private val _tweets = MutableLiveData<List<Tweet>>()
    val tweets: LiveData<List<Tweet>>
        get() = _tweets

    val tweets2: LiveData<List<Tweet>>
        get() = Transformations.map(_tweets) {
            it
        }

    fun setNewTweet(tweetList: List<Tweet>) {
        _tweets.value = tweetList
    }

    fun setTweetFromLocal() {
        viewModelScope.launch {
            _tweets.value = fetchTweetsUseCase.invoke()
        }
    }
}