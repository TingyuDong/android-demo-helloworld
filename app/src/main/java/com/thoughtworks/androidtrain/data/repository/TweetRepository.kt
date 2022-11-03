package com.thoughtworks.androidtrain.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.thoughtworks.androidtrain.data.source.local.room.entity.Tweet

interface TweetRepository {

    fun fetchTweets(): List<Tweet>
}