package com.thoughtworks.androidtrain

import androidx.lifecycle.*
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
        runBlocking {
            println("[当前viewModel外线程为：${Thread.currentThread().name} ${Thread.currentThread().id}]")
            viewModelScope.launch {
                println("[当前viewModel内launch线程为：${Thread.currentThread().name} ${Thread.currentThread().id}]")
                _tweets.value = fetchTweetsUseCase.invoke()
            }.join()
        }
    }
}