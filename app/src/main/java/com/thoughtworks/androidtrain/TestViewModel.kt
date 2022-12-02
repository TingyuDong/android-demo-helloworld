package com.thoughtworks.androidtrain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.thoughtworks.androidtrain.data.model.Tweet

class TestViewModel : ViewModel() {

    private val _tweets = MutableLiveData<List<Tweet>>()
    val tweets: LiveData<List<Tweet>>
        get() = _tweets

    val tweets2: LiveData<List<Tweet>>
        get() = Transformations.map(_tweets){
            it
        }

    fun setNewTweet(tweetList: List<Tweet>) {
        _tweets.value = tweetList
    }
}