package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Tweet

interface RepositoryInterface {
    fun fetchTweets(): List<Tweet>
    fun addTweet(tweet: Tweet)
    fun addAllTweet(tweets: ArrayList<Tweet>)
}