package com.thoughtworks.androidtrain.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Image
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet

interface TweetRepository {
    fun fetchTweets(): List<Tweet>
    fun getSender(senderId: Int): Sender?
    fun getImages(tweetId: Int): List<Image>?
    fun getComments(tweetId: Int): List<Comment>?
}